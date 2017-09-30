package com.sedsoftware.yaptalker.data.model

import pl.droidsonroids.jspoon.annotation.Selector

class UserProfile {
  @Selector("div[id=profilename]")
  lateinit var nickname: String
  @Selector("td:containsOwn(Аватар) + td", attr = "src", defValue = "//www.yaplakal.com/html/static/noavatar.gif")
  lateinit var avatar: String
  @Selector("img:has([alt=User Photo])", attr = "src", defValue = "")
  lateinit var photo: String
  @Selector("td:containsOwn(Группа) + td", defValue = "")
  lateinit var group: String
  @Selector("td:containsOwn(Статус) + td", defValue = "")
  lateinit var status: String
  @Selector(".uq > span")
  lateinit var uq: String
  @Selector("td:containsOwn(Подпись) + td", defValue = "", attr = "innerHtml")
  lateinit var signature: String
  @Selector("td:containsOwn(Награды) + td", defValue = "", attr = "innerHtml")
  lateinit var rewards: String
  @Selector("td:containsOwn(Регистрация) + td", defValue = "")
  lateinit var registerDate: String
  @Selector("td:containsOwn(Часовой пояс) + td", defValue = "")
  lateinit var timeZone: String
  @Selector("td:containsOwn(Вебсайт) + td", defValue = "")
  lateinit var website: String
  @Selector("td:containsOwn(Дата рождения) + td", defValue = "")
  lateinit var birthDate: String
  @Selector("td:containsOwn(Место жительства) + td", defValue = "")
  lateinit var location: String
  @Selector("td:containsOwn(Увлечения) + td", defValue = "", attr = "innerHtml")
  lateinit var interests: String
  @Selector("td:containsOwn(Половая принадлежность) + td", defValue = "")
  lateinit var sex: String
  @Selector("td:containsOwn(Всего полезных сообщений) + td", defValue = "0")
  lateinit var messagesCount: String
  @Selector("td:containsOwn(Сообщений в день) + td", defValue = "0")
  lateinit var messsagesPerDay: String
  @Selector("td:containsOwn(Всего баянов) + td", defValue = "0")
  lateinit var bayans: String
  @Selector("td:containsOwn(Создано тем сегодня) + td", defValue = "0")
  lateinit var todayTopics: String
  @Selector("td:containsOwn(E-mail + td", defValue = "-")
  lateinit var email: String
  @Selector("td:containsOwn(Номер ICQ) + td", defValue = "-")
  lateinit var icq: String
}
