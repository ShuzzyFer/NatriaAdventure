package com.shuzzy.natriaadventure

import android.content.Context
import android.content.Intent
import android.view.View
import com.shuzzy.natriaadventure.AppState.fought
import java.io.BufferedReader
import java.io.InputStreamReader

object AppState {
    var fought: Boolean = false
    var archery: Boolean = false
    var archeryWon: Boolean = false
    var boughtItem: Boolean = false
    var tookQuest: Boolean = false
}

class Story(private val context: Context, private val gameScreen: GameScreen) {
    // Переменные состояния для отслеживания выполнения действий
    private var boughtPotion = false
    private var boughtTreasureMap = false
    private var acceptedMercenaryQuest = false

    fun StartingPoint() {
        gameScreen.text.text = "Вы — Шазз, бесстрашный авантюрист, чьи приключения уже стали легендами. Сегодня вы оказались на пути в королевство Натрия — земли, полные тайн и возможностей. На горизонте вы видите величественные стены города Нирон, обнесённого высокими башнями. Там, среди гомона людей, можно найти всё, что душе угодно: от магических товаров до захватывающих слухов о древних квестах. Природа вокруг полна звуков лесов и холмов, но дух авантюриста тянет вас к шуму города."
        gameScreen.image.setImageResource(R.drawable.kingdom)

        gameScreen.button1.text = "Пойти в город Нирон"
        gameScreen.button2.text = "Осмотреть окрестности"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener {
            goToCity()
        }

        gameScreen.button2.setOnClickListener {
            exploreSurroundings()
        }
    }

    private fun goToCity() {
        gameScreen.text.text = "Город Нирон встречает вас шумом улиц, торговыми рядами и суетой горожан. Вы чувствуете запах свежевыпеченного хлеба и жареного мяса, в то время как продавцы зазывают вас к своим лавкам. В центре города возвышается таверна 'Хмельной Грифон', знаменитая своими легендами и теплыми вечерами. Однако рынок 'Лунный Базар', полный экзотических товаров и артефактов, также привлекает ваше внимание. Вы решаете, куда направить свои шаги."
        gameScreen.image.setImageResource(R.drawable.city)

        gameScreen.button1.text = "Посетить таверну 'Хмельной Грифон'"
        gameScreen.button2.text = "Пойти на рынок 'Лунный Базар'"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener {
            goToTavern()
        }

        gameScreen.button2.setOnClickListener {
            goToMarket()
        }
    }

    private fun goToTavern() {
        gameScreen.text.text = "Таверна 'Хмельной Грифон' полна жизни: смех, гул голосов и звук музыкальных инструментов заполняют воздух. В углу, под густой тенью камина, вы замечаете угрюмого наемника, чья фигура скрыта плащом. Его пронизывающий взгляд цепляет ваше внимание. Хозяин заведения, старый знакомец, неумолкаемо рассказывает гостям историю о древних картах сокровищ, которая уже стала притчей на устах завсегдатаев."
        gameScreen.image.setImageResource(R.drawable.tavern)

        gameScreen.button1.text = "Поговорить с наемником"
        gameScreen.button2.text = "Поговорить с хозяином таверны"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener {
            talkToMercenary()
        }

        gameScreen.button2.setOnClickListener {
            talkToTavernOwner()
        }
    }

    private fun goToMarket() {
        gameScreen.text.text = "Лунный Базар бурлит жизнью: мерцающие магические огни освещают торговые лавки, где можно встретить самых экзотических купцов. Странные запахи и звуки наполняют воздух. На прилавках выставлены таинственные зелья, магические артефакты и странные существа в клетках. Среди предложений вы замечаете бутылку с сияющим внутри зельем и древний свиток, покрытый загадочными символами. Возможно, они могут быть вам полезны"
        gameScreen.image.setImageResource(R.drawable.market)

        gameScreen.button1.text = "Купить зелье силы"
        gameScreen.button2.text = "Купить карту сокровищ"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        // Проверяем доступность действий
        gameScreen.button1.isEnabled = !boughtPotion
        gameScreen.button2.isEnabled = !boughtTreasureMap

        gameScreen.button1.setOnClickListener {
            buyPotion()
        }

        gameScreen.button2.setOnClickListener {
            buyTreasureMap()
        }
    }

    private fun talkToMercenary() {
        gameScreen.text.text = "Наемник пристально смотрит на вас, его глаза сверкают из-под капюшона. Его голос звучит низко и глухо, как будто из глубин подземелий. 'Я слышал, ты ищешь нечто большее, чем простые приключения', — говорит он, наклоняясь ближе. 'В подземельях за городом спрятан артефакт великой силы. Лишь немногие решались спуститься туда, но те, кто возвращались, были богаты... или мертвы."
        gameScreen.button1.text = "Принять задание"
        gameScreen.button2.text = "Отказаться"
        gameScreen.button1.setOnClickListener {
            acceptMercenaryQuest()
        }
        gameScreen.button2.setOnClickListener {
            declineMercenaryQuest()
        }
    }

    private fun talkToTavernOwner() {
        gameScreen.text.text = "Хозяин таверны, протирая кружку, наклоняется к вам и с заговорщицкой улыбкой шепчет: 'Ты когда-нибудь слышал о комнате под таверной? Там, в глубине, спрятана карта древнего подземелья. Мало кто о ней знает, но я уверен — она ведет к чему-то по-настоящему ценному.' Его глаза блестят от любопытства, как будто он сам давно жаждет разгадать эту тайну."
        gameScreen.button1.text = "Спуститься в подвал"
        gameScreen.button2.text = "Поблагодарить и уйти"
        gameScreen.button1.setOnClickListener {
            exploreTavernBasement()
        }
        gameScreen.button2.setOnClickListener {
            goToCity()
        }
    }

    private fun buyPotion() {
        gameScreen.text.text = "Вы приобрели флакон с ярким светящимся зельем. Оно весит неожиданно тяжело для своего размера, а внутри, кажется, бурлит чистая магическая энергия. Это зелье может подарить вам невероятную силу в самые трудные моменты. Вы решаете, что его стоит использовать с умом."
        boughtPotion = true
        goToCity()
    }

    private fun buyTreasureMap() {
        gameScreen.text.text = "Карта сокровищ выглядит старинной: пожелтевший пергамент покрыт потускневшими чернилами и странными отметками. Она ведет к загадочным местам, о которых никто не говорит вслух. Ваше сердце замирает от мысли, что эта карта может стать ключом к открытиям, которые изменят судьбу."
        boughtTreasureMap = true
        goToCity()
    }

    private fun acceptMercenaryQuest() {
        gameScreen.text.text = "Приняв предложение наемника, вы чувствуете, как адреналин заливает ваши вены. Путь в подземелье предвещает опасности, но и великие награды. Наемник кивает вам и, не говоря больше ни слова, скрывается в тени. У вас появляется странное чувство, что это задание изменит ход вашего пути"
        gameScreen.image.setImageResource(R.drawable.entrance)
        acceptedMercenaryQuest = true
        gameScreen.button1.text = "Войти в подземелье"
        gameScreen.button2.text = "Вернуться в город"
        gameScreen.button1.setOnClickListener {
            enterDungeon()
        }
        gameScreen.button2.setOnClickListener {
            goToCity()
        }
    }

    private fun declineMercenaryQuest() {
        gameScreen.text.text = "Вы отказались от задания. Наемник хмурится, но не настаивает."
        goToCity()
    }

    private fun exploreTavernBasement() {
        gameScreen.text.text = "Вы спустились в подвал и нашли старинную карту с отметками, указывающими на путь к подземелью."
        gameScreen.button1.text = "Вернуться в таверну"
        gameScreen.button2.text = "Отправиться к подземелью"
        gameScreen.button1.setOnClickListener {
            goToTavern()
        }
        gameScreen.button2.setOnClickListener {
            enterDungeon()
        }
    }

    private fun enterDungeon() {
        gameScreen.text.text = "Вы входите в темное подземелье. Карта и зелье помогут преодолеть опасности на пути."
        gameScreen.image.setImageResource(R.drawable.dungeon)
        gameScreen.button1.text = "Идти вперед"
        gameScreen.button2.visibility = View.INVISIBLE
        gameScreen.button1.setOnClickListener {
            advanceThroughDungeon()
        }
    }

    private fun advanceThroughDungeon() {
        if(boughtPotion==false && boughtTreasureMap==false) {
            gameScreen.text.text = "Вы продвигаетесь по темным коридорам подземелья, ощупывая стену, покрытую древними символами. Откуда-то впереди доносятся зловещие звуки: шорохи, эхо и отдалённые удары. Путь становится всё мрачнее, но в сердце загорается жажда разгадки. Без карты и зелья это путешествие кажется рискованным, но настоящие герои всегда находят выход."
            gameScreen.button1.text = "Вернуться в город"
            gameScreen.button2.text = "Применить зелье силы"
            gameScreen.button2.visibility = View.INVISIBLE

            gameScreen.button1.setOnClickListener {
                gameScreen.button1.isEnabled = true
                gameScreen.button2.isEnabled = true
                gameScreen.button2.visibility = View.VISIBLE
                goToCity()
            }

        }
        else {
            gameScreen.text.text =
                "Продвигаясь вперед, вы замечаете странные символы на стенах и слышите звуки из глубины подземелья. Что вы хотите сделать дальше?"
            gameScreen.button1.text = "Следовать по карте"
            gameScreen.button2.text = "Применить зелье силы"
            gameScreen.button2.visibility = View.VISIBLE

            // Проверяем доступность использования предметов
            gameScreen.button1.isEnabled = boughtTreasureMap
            gameScreen.button2.isEnabled = boughtPotion

            gameScreen.button1.setOnClickListener {
                gameScreen.button1.isEnabled = true
                gameScreen.button2.isEnabled = true
                followMap()
            }
            gameScreen.button2.setOnClickListener {
                gameScreen.button1.isEnabled = true
                gameScreen.button2.isEnabled = true
                usePotion()
            }
        }
    }

    private fun followMap() {
        gameScreen.text.text = "С помощью карты вы находите потайной ход, ведущий к центру подземелья. Перед вами массивные двери с древними знаками."
        gameScreen.button1.text = "Открыть двери"
        gameScreen.button2.text = "Вернуться назад"
        gameScreen.button1.setOnClickListener {
            openDoors()
        }
        gameScreen.button2.setOnClickListener {
            enterDungeon()
        }
    }

    private fun usePotion() {
        gameScreen.text.text = "Зелье силы придает вам мощь и уверенность. Вы готовы встретить любые опасности."
        followMap()
    }

    private fun openDoors() {
        gameScreen.text.text = "Открывая массивные двери, вы видите святилище, где на пьедестале лежит древний артефакт. Взяв его, вы чувствуете приток силы."
        gameScreen.button1.text = "Вернуться в город"
        //gameScreen.button2.visibility = View.INVISIBLE
        gameScreen.button1.setOnClickListener {
            goToCityWithArtifact()
        }
    }

    private fun goToCityWithArtifact() {
        gameScreen.text.text = "Вернувшись в город с артефактом, вы чувствуете себя героем, а ваше имя будет воспето в легендах."
        gameScreen.button1.text = "Начать заново"
        gameScreen.button1.setOnClickListener {
            StartingPoint()
        }
    }

    private fun exploreSurroundings() {
        gameScreen.text.text = "Вы осматриваете окрестности, но ничего интересного не находите и решаете отправиться в город Нирон."
        goToCity()
    }
}


