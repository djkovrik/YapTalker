package com.sedsoftware.yaptalker.data.parsed

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed user profile in data layer.
 */
@Suppress("VariableMinLength")
class UserProfileParsed {
    @Selector(value = "div[id=profilename]", defValue = "")
    lateinit var nickname: String
    @Selector(value = ".row1 > img", attr = "src", defValue = "//www.yaplakal.com/html/static/noavatar.gif")
    lateinit var avatar: String
    @Selector(value = ".tablebasic img", attr = "src", defValue = "")
    lateinit var photo: String
    @Selector(value = "td:contains(Группа) + td", defValue = "")
    lateinit var group: String
    @Selector(value = "td:contains(Статус) + td", defValue = "")
    lateinit var status: String
    @Selector(value = ".uq > span", defValue = "0")
    lateinit var uq: String
    @Selector(value = "td:contains(Подпись) + td", attr = "innerHtml", defValue = "")
    lateinit var signature: String
    @Selector(value = "td:contains(Награды) + td", attr = "innerHtml", defValue = "")
    lateinit var rewards: String
    @Selector(value = "td:contains(Регистрация) + td", defValue = "")
    lateinit var registerDate: String
    @Selector(value = "td:contains(Часовой пояс) + td", defValue = "")
    lateinit var timeZone: String
    @Selector(value = "td:contains(Вебсайт) + td", defValue = "")
    lateinit var website: String
    @Selector(value = "td:contains(Дата рождения) + td", defValue = "")
    lateinit var birthDate: String
    @Selector(value = "td:contains(Место жительства) + td", defValue = "")
    lateinit var location: String
    @Selector(value = "td:contains(Увлечения) + td", defValue = "")
    lateinit var interests: String
    @Selector(value = "td:contains(Половая принадлежность) + td", defValue = "")
    lateinit var sex: String
    @Selector(value = "td:contains(Всего полезных сообщений) + td", regex = "([-\\d]+)", defValue = "0")
    lateinit var messagesCount: String
    @Selector(value = "td:contains(Сообщений в день) + td", defValue = "0")
    lateinit var messsagesPerDay: String
    @Selector(value = "td:contains(Всего баянов) + td", defValue = "0")
    lateinit var bayans: String
    @Selector(value = "td:contains(Создано тем сегодня) + td", regex = "(\\d+)", defValue = "0")
    lateinit var todayTopics: String
    @Selector(value = "td:contains(E-mail + td)", defValue = "")
    lateinit var email: String
    @Selector(value = "td:contains(Номер ICQ) + td", defValue = "")
    lateinit var icq: String
}
