package com.sedsoftware.data.parsing

import pl.droidsonroids.jspoon.annotation.Selector

/**
 * Class which represents parsed user profile in data layer.
 */
class UserProfileParsed {
  @Selector("div[id=profilename]", defValue = "")
  lateinit var nickname: String
  @Selector(".row1 > img", attr = "src", defValue = "//www.yaplakal.com/html/static/noavatar.gif")
  lateinit var avatar: String
  @Selector(".tablebasic img", attr = "src", defValue = "")
  lateinit var photo: String
  @Selector("td:contains(Группа) + td", defValue = "")
  lateinit var group: String
  @Selector("td:contains(Статус) + td", defValue = "")
  lateinit var status: String
  @Selector(".uq > span", defValue = "0")
  lateinit var uq: String
  @Selector("td:contains(Подпись) + td", attr = "innerHtml", defValue = "")
  lateinit var signature: String
  @Selector("td:contains(Награды) + td", attr = "innerHtml", defValue = "")
  lateinit var rewards: String
  @Selector("td:contains(Регистрация) + td", defValue = "")
  lateinit var registerDate: String
  @Selector("td:contains(Часовой пояс) + td", defValue = "")
  lateinit var timeZone: String
  @Selector("td:contains(Вебсайт) + td", defValue = "")
  lateinit var website: String
  @Selector("td:contains(Дата рождения) + td", defValue = "")
  lateinit var birthDate: String
  @Selector("td:contains(Место жительства) + td", defValue = "")
  lateinit var location: String
  @Selector("td:contains(Увлечения) + td", defValue = "")
  lateinit var interests: String
  @Selector("td:contains(Половая принадлежность) + td", defValue = "")
  lateinit var sex: String
  @Selector("td:contains(Всего полезных сообщений) + td", format = "(\\d+)", defValue = "0")
  lateinit var messagesCount: String
  @Selector("td:contains(Сообщений в день) + td", defValue = "0")
  lateinit var messsagesPerDay: String
  @Selector("td:contains(Всего баянов) + td", defValue = "0")
  lateinit var bayans: String
  @Selector("td:contains(Создано тем сегодня) + td", format = "(\\d+)", defValue = "0")
  lateinit var todayTopics: String
  @Selector("td:contains(E-mail + td)", defValue = "")
  lateinit var email: String
  @Selector("td:contains(Номер ICQ) + td", defValue = "")
  lateinit var icq: String
}
