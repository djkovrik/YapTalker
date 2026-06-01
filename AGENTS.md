# AGENTS.md

This file captures project-specific guidance for coding agents working in this repository.

## Project Overview

YapTalker is a legacy Android application that has been migrated to a modern Android build stack while keeping most of its original architecture and libraries intact.

Current build baseline:

- Gradle wrapper: `gradle-8.13-bin.zip`
- Android Gradle Plugin: `8.13.2`
- Kotlin: `2.3.21`
- `compileSdk`: `36`
- `targetSdk`: `36`
- `minSdk`: `23`

Shared Android SDK and app version values are declared in `gradle/project-config.gradle`.
Dependency versions and plugin aliases are declared in `gradle/libs.versions.toml`.

Do not reintroduce `buildsystem/dependencies.gradle`; dependency management has been migrated to the Gradle version catalog.

## Useful Commands

Run focused checks while editing:

```powershell
.\gradlew.bat :data:compileDevDebugKotlin :app:assembleDevDebug --stacktrace
```

Run a release build check when touching Gradle, manifests, resources, or code shrinking related files:

```powershell
.\gradlew.bat :app:assembleRelease --stacktrace
```

Use the existing Gradle wrapper. Do not assume a globally installed Gradle version.

## Repository Conventions

- Prefer small, scoped changes that match existing package and module boundaries.
- The app still uses RxJava 2, Dagger 2, Moxy, Retrofit, jspoon, and synthetic view access compatibility stubs.
- Do not remove the local `kotlinx.android.synthetic...` compatibility sources unless the touched screens are fully migrated to ViewBinding or another replacement.
- Keep local secrets and developer-specific settings out of source control. `gradle.properties` is intentionally ignored.
- Temporary investigation directories such as `.codex-tmp` should not be committed. Delete them when done.

## API And Web Session Notes

The app is gradually moving critical flows from HTML/web form requests on `www.yaplakal.com` to the mobile API on `https://api.yaplakal.com/`.

Keep this section current whenever auth, cookies, posting, rating, or API/web routing changes. Future agents should be able to tell from this file which flows are API-backed and which still depend on jspoon/web parsing.

API-backed flows currently include:

- Login via `GET action/login`.
- Logout via `GET action/logout`, with fallback to the legacy web logout.
- Current user lookup via `GET settings`, with fallback to cached API login profile data when `settings` fails.
- New comment posting via `POST action/comment`, including multipart image upload.
- Post/topic rating via `GET action/rank`.

Web/HTML-backed flows currently include:

- Topic, forum, news, active topic, and other feed/page loading through `YapLoader` + jspoon/jsoup parsers.
- Quote text loading and edit text loading.
- Edited post submission through the legacy web form.
- Site preferences under `UserCP`.
- `/forum` user parsing as a last-resort fallback only when no cached API login profile exists.

The current auth approach is not a fake local login. A successful API login returns real `SID`, `auth_key`, user id, nickname, rank, avatar, and mail data. The repository saves:

- `SID` through `CookieStorage`, so API and compatible web/file requests can send `Cookie: SID=...`.
- `LoginSessionInfo` in `Settings`, so the navigation drawer and API-only actions still have real user data if `GET settings` returns `403`.

This cache exists because a real Pixel 10 Pro on Android 16 has been observed to complete `action/login` successfully while `settings` still returns `403`; emulators may not reproduce it. Do not replace this with guest data or with a synthetic session. Clear the cached profile on logout and before a new login attempt.

The API OkHttp client intentionally follows the unofficial app's network shape:

- It uses `HeaderAndParamManipulationInterceptor` to add `md5`, `appVersion`, `type=json`, `User-Agent`, and `http-udid`.
- `YapTalkerApp.getAppVersion()` is pinned to the mobile API version expected by the server.
- `Ipv4OnlyDns` is used for the API client, matching the unofficial app.
- `Skip-Saved-Cookies` is used for `action/login` so stale locally saved SID values do not poison login; do not remove this without retesting Pixel/Android 16 auth.
- `SendSavedCookiesInterceptor` must not overwrite cookies already prepared by OkHttp's `CookieJar`; `CookieStorage` is a fallback for requests that have no Cookie header.
- Empty web `Set-Cookie: SID=` values must not clear the saved API SID. Explicit logout should clear it.

The decompiled source of an unofficial Yaplakal app is available locally and should be used as a reference before inventing API behavior:

```text
D:\Sources\Android\ru.swc.yaplakalcom
```

Start API research from:

```text
D:\Sources\Android\ru.swc.yaplakalcom\interfaces\Api.java
D:\Sources\Android\ru.swc.yaplakalcom\App.java
D:\Sources\Android\ru.swc.yaplakalcom\utils\HeaderAndParamManipulationInterceptor.java
D:\Sources\Android\ru.swc.yaplakalcom\network\DnsSelector.java
```

When migrating more web flows to API, prefer matching the decompiled client's endpoint, HTTP method, query/form fields, cookie handling, and model names first, then map the API model into the app's existing domain/presentation models.

## jspoon Parsing Notes

HTML parsing lives mostly under:

```text
data/src/main/java/com/sedsoftware/yaptalker/data/parsed
```

The project uses `pl.droidsonroids.jspoon.annotation.Selector` classes consumed by Retrofit through `JspoonConverterFactory`.

Important details:

- Prefer selectors based on stable structure, classes, ids, and href patterns.
- Avoid selectors that depend on visible Russian text or `title` strings when a structural selector is available. Site text and encoding have changed before.
- Be careful with `innerHtml`: empty action links often contain only `&nbsp;`, so flag-like fields may need `attr = "outerHtml"` to detect presence.
- Use `defValue` for all nullable-looking scalar fields to keep parsing failures from crashing mapping code.
- When parsing post ids, prefer stable markers or attributes from the actual HTML. Current topic pages expose ids through comments like `THE POST 150285603` inside `td.post2`.
- Keep real HTML snapshots such as `karma.html` useful for regression checks. When parser behavior changes, verify against the relevant snapshot and then run at least `:data:compileDevDebugKotlin`.

Known parsing patterns:

- Rating availability uses `a.post-plus` and `a.post-minus` with `attr = "outerHtml"`.
- Already-clicked rating state uses `span.post-plus-clicked` and `span.post-minus-clicked` with `attr = "outerHtml"`.
- Quote availability uses `a.reply-icon`; the visible label may be "Reply to comment" in Russian rather than the old "quote" text.
- Topic pages use `table.comment-table[id~=entry\d+]:has(.normalname)` for posts, not the old `p_row_*` table ids.
- Topic and forum navigation use the new pager markup: current page is `td.pager > span.pager-current`, and total item count is read from `a.page-jump` `onclick`. Mappers convert total posts/topics to total pages.
- Topic rating target ids are parsed from `div[rel=rating] a[onclick~=doRatePost]` using `outerHtml`.
- Forum and active topic answer counts may contain spaces; normalize them before converting to integers.
- News list blocks use `td.topic-header[id^=topic_]`, `td.postcolor.news-content[id^=news_]`, and `td.holder.newsbottom.desc`.
- Active topic/forum dates now use Russian month names, for example `day month-name year at HH:mm`; do not assume the old `dd.MM.yyyy - HH:mm` format.
- Post content quote parsing depends on `table:has(td#QUOTE)` and recursively handles nested quotes before removing quote tables from the main post text.

## Iconics Notes

The project uses the legacy Iconics/MaterialDrawer combination:

- `com.mikepenz:materialdrawer:6.1.1`
- `com.mikepenz:iconics-core:3.2.0-rc1`
- `com.mikepenz:iconics-views:3.2.0-rc1`
- `com.mikepenz:community-material-typeface:3.1.0-rc02`
- `com.mikepenz:typeicons-typeface:2.0.7.5`

Do not wrap activities with `IconicsContextWrapper`; it caused hidden API reflection crashes on modern target SDKs.

Inline strings like `{cmd-thumb-up}` are unreliable without the Iconics layout inflater wrapper. For UI that must render icons reliably, set drawables or typefaces explicitly in Kotlin.

Useful local helpers:

- `TextView.setStartIcon(...)`
- `CharSequence.withoutIconicsTokens()`
- `FloatingActionButton.setIconicsImage(...)`

Some Community Material icons are split between `CommunityMaterial.Icon` and `CommunityMaterial.Icon2` in this legacy typeface version. Check the actual generated enum before assuming an icon's namespace.

## Navigation Notes

The app uses a local minimal implementation of Cicerone under:

```text
app/src/main/java/ru/terrakok/cicerone
```

This implementation intentionally buffers navigation commands until a navigator is attached. Preserve that behavior: initial navigation from `MainActivityPresenter.navigateToDefaultHomePage()` depends on it during app startup.

## Android Manifest Notes

With target SDK 36, every component with an intent filter must explicitly define `android:exported`.

Keep exported components conservative:

- Launcher activity: `android:exported="true"`
- Internal activities, services, and receivers: `android:exported="false"` unless Android or an external integration requires otherwise.

## Edge-To-Edge Notes

`BaseActivity` enables edge-to-edge by default. `MainActivity` explicitly opts out and should keep the old system-window behavior unless its navigation drawer/app bar layout is intentionally redesigned.

For new activities:

- Fullscreen/media activities can use the default transparent status bar.
- Regular toolbar activities should override `edgeToEdgeStatusBarColorAttr` with `R.attr.colorPrimaryDark` so `BaseActivity` adds a matching status bar scrim instead of showing the root layout background.
- Avoid setting `fitsSystemWindows` in layouts unless there is a screen-specific reason; prefer the centralized insets handling in `BaseActivity`.

## Dependency Updates

When updating dependencies:

- Edit `gradle/libs.versions.toml`.
- Keep related transitive compatibility in mind. For example, MaterialDrawer and Iconics versions must stay binary-compatible.
- Prefer targeted upgrades with a build check after each riskier group.
- If a dependency migration requires source changes, document the local convention here if future agents are likely to trip over it.
