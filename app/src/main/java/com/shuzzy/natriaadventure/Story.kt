package com.shuzzy.natriaadventure

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shuzzy.natriaadventure.AppState.archery

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
    private var boughtPotion = false
    private var boughtTreasureMap = false
    private var acceptedMercenaryQuest = false
    private lateinit var achievManager: AchievManager

    fun StartingPoint() {
        gameScreen.text.text = "Вы — Шазз, авантюрист с душой исследователя, знаменитый своими приключениями, которые пересказывают у каждого костра. Сегодня ваш путь лежит в земли королевства Натрия, где среди тенистых лесов, бурных рек и древних руин скрываются несметные богатства и могущественные артефакты. На горизонте, окутанный дымкой, возвышается город Нирон, известный как сердце Натрии. От него веет запахом свободы и опасности. Гул людской толпы доносится даже до ваших ушей, и вам становится ясно: приключение зовет, и откликнуться на него должен именно вы."

        gameScreen.image.setImageResource(R.drawable.kingdom)

        gameScreen.button1.text = "Пойти в город Нирон"
        gameScreen.button2.text = "Осмотреть окрестности"
        gameScreen.button3.text = "Заглянуть на поле неподалеку"
        gameScreen.button4.text = "Зайти в тир около дороги и проверить свои навыки стрельбы"

        gameScreen.button1.setOnClickListener { goToCity() }
        gameScreen.button2.setOnClickListener { exploreSurroundings() }
        gameScreen.button3.setOnClickListener {Fight()}
        gameScreen.button4.setOnClickListener{Archery()}

        if(fought==true)
            gameScreen.button3.visibility = View.INVISIBLE;
        if(archery==true)
            gameScreen.button4.visibility = View.INVISIBLE
    }

    fun Fight() {
        val intent = Intent(gameScreen, Fight::class.java)
        startActivity(context, intent, null)
        fought = true;

        StartingPoint()

    }

    fun Archery() {
        val intent = Intent(gameScreen, CityActivity::class.java)
        startActivity(context, intent, null)

        archery=true
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Bow").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        StartingPoint()

    }

    fun goToCity() {
        gameScreen.text.text = "Город Нирон раскрывается перед вами, как шкатулка с драгоценностями. Узкие улочки, полные ремесленных лавок и таверн, заполнены криками торговцев и веселым смехом горожан. На каждом углу виднеются удивительные сооружения — от средневековых башен до ярких стен со сложными узорами. Люди толпятся возле рынка и таверны 'Хмельной Грифон'. Откуда-то доносится музыка, словно приглашая присоединиться к веселому танцу жизни в Нироне. Но вы знаете, что под шумной поверхностью скрываются мрачные тайны и могущественные секреты, которые ждут своего открытия."

        gameScreen.image.setImageResource(R.drawable.city)

        gameScreen.button1.text = "Посетить таверну"
        gameScreen.button2.text = "Посетить рынок"
        gameScreen.button3.text = "Пойти к городской стене"
        gameScreen.button4.text = "Посетить храм"
        gameScreen.button3.visibility = View.VISIBLE
        gameScreen.button4.visibility = View.VISIBLE

        gameScreen.button1.setOnClickListener { goToTavern() }
        gameScreen.button2.setOnClickListener { goToMarket() }
        gameScreen.button3.setOnClickListener { visitCityWall() }
        gameScreen.button4.setOnClickListener { visitTemple() }
    }

    private fun visitCityWall() {
        gameScreen.text.text = "Вы подходите к мощным стенам города, на которых оживленно переговариваются стражники. Лица их суровы, а голоса звучат тревожно. В разговоре вы улавливаете намек на грядущий набег жестоких разбойников, которые планируют атаковать город под покровом ночи. Глядя на подготовку к защите, вы понимаете, что могли бы оказать неоценимую помощь."
        gameScreen.button1.text = "Помочь стражникам"
        gameScreen.button2.text = "Вернуться"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener { helpGuards() }
        gameScreen.button2.setOnClickListener { goToCity() }
    }

    private fun visitTemple() {
        gameScreen.text.text = "Тихое величие храма окутывает вас, как только вы переступаете порог. Внутри, окруженный светом свечей, стоит мудрый старец. Его взгляд устремлен вдаль, но при вашем приближении он обращает внимание на вас и предлагает испытание: разгадать древнюю загадку, что веками не давала покоя паломникам. В случае успеха он готов вручить вам редкий амулет, обладающий таинственной силой."
        gameScreen.button1.text = "Принять задание мудреца"
        gameScreen.button2.text = "Отказаться и уйти"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener { solveTempleRiddle() }
        gameScreen.button2.setOnClickListener { goToCity() }
    }

    private fun goToTavern() {
        gameScreen.text.text = "Таверна 'Хмельной Грифон' встречает вас уютом и теплом. Пахнет хлебом, медом и пряностями. Сидя за столом, у камина, вы замечаете одинокого наемника, чьи глаза сверкают в полутьме, словно светлячки. Он тянет к вам, как магнитом, но в то же время внушает уважение и даже страх. Хозяин таверны, бодрый старик с густой бородой, склоняется к очередной компании, рассказывая невероятную историю о древних временах. В каждом его слове скрыта легенда, и становится ясно: здесь собрались те, кто готов продать, купить или открыть свою душу за бесценок."

        gameScreen.button1.text = "Поговорить с наемником"
        gameScreen.button2.text = "Поговорить с хозяином"
        gameScreen.button3.text = "Послушать местные истории"
        gameScreen.button4.text = "Поиграть в карты"
        gameScreen.button3.visibility = View.VISIBLE
        gameScreen.button4.visibility = View.VISIBLE
        gameScreen.image.setImageResource(R.drawable.tavern)
        gameScreen.button1.setOnClickListener { talkToMercenary() }
        gameScreen.button2.setOnClickListener { talkToTavernOwner() }
        gameScreen.button3.setOnClickListener { listenToLocalStories() }
        gameScreen.button4.setOnClickListener { playCards() }
    }

    private fun goToMarket() {
        gameScreen.text.text = "Лунный Базар — это волшебное место, где каждая лавка и каждый купец привносят в атмосферу частичку магии. На витринах сверкают драгоценные камни, выложенные в сложные узоры, а от одного взгляда на них захватывает дух. Зелья разного цвета переливаются и бурлят в бутылочках, а древние свитки словно переносят вас в другие времена и пространства. Один из продавцов, старик в странных одеждах, предлагет вам древний свиток, который, по его словам, содержит тайну королевского рода. В воздухе витает обещание силы и неизвестности, но за все это придется заплатить."

        gameScreen.button1.text = "Купить зелье силы"
        gameScreen.button2.text = "Купить карту сокровищ"
        gameScreen.button3.text = "Посетить лавку старьевщика"
        gameScreen.button4.text = "Поговорить с магом"
        gameScreen.button3.visibility = View.VISIBLE
        gameScreen.button4.visibility = View.VISIBLE
        gameScreen.image.setImageResource(R.drawable.market)

        gameScreen.button1.isEnabled = !boughtPotion
        gameScreen.button2.isEnabled = !boughtTreasureMap

        gameScreen.button1.setOnClickListener { buyPotion() }
        gameScreen.button2.setOnClickListener { buyTreasureMap() }
        gameScreen.button3.setOnClickListener { visitJunkShop() }
        gameScreen.button4.setOnClickListener { talkToMage() }
    }

    private fun helpGuards() {
        gameScreen.text.text = "Вы помогаете стражникам и получаете награду за смелость."
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Help").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться в город"
        gameScreen.button1.setOnClickListener { goToCity() }
    }

    private fun solveTempleRiddle() {
        gameScreen.text.text = "Вы успешно разгадываете загадку мудреца и получаете амулет, который даст вам преимущество в подземелье."
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Amulet").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться в город"
        gameScreen.button1.setOnClickListener { goToCity() }
    }

    private fun listenToLocalStories() {
        gameScreen.text.text = "Вы слушаете рассказы о дальних землях и сокровищах, что могут привести к новым приключениям."
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Stories").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться к таверне"
        gameScreen.button1.setOnClickListener { goToTavern() }
    }

    private fun playCards() {
        gameScreen.text.text = "Вы играете в карты и выигрываете странный амулет, который, по слухам, обладает магической силой."
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Player").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться к таверне"
        gameScreen.button1.setOnClickListener { goToTavern() }
    }

    private fun visitJunkShop() {
        gameScreen.text.text = "Старьевщик предлагает вам редкие предметы, такие как компас и магические камни."
        gameScreen.button1.text = "Купить компас"
        gameScreen.button2.text = "Вернуться на рынок"
        gameScreen.button1.setOnClickListener { buyCompass() }
        gameScreen.button2.setOnClickListener { goToMarket() }
    }

    private fun talkToMage() {
        gameScreen.text.text = "Маг предлагает помочь в поиске ингредиентов для зелья, которые находятся в опасных местах."
        gameScreen.button1.text = "Принять задание"
        gameScreen.button2.text = "Вернуться на рынок"
        gameScreen.button1.setOnClickListener { acceptMageQuest() }
        gameScreen.button2.setOnClickListener { goToMarket() }
    }

    private fun buyPotion() {
        gameScreen.text.text = "Флакон с зельем пульсирует, словно живой. Внутри, как кажется, течет чистая магия. Вы чувствуете тепло, проникающее в руки, как только держите его. Продавец предупреждает: 'Используй это с умом. Оно придаст тебе силу, но она продлится лишь мгновение. И помни: каждая капля ценнее золота.' Вы решаете, что это зелье — не просто напиток, но возможность выжить в самых темных местах."

        boughtPotion = true
        goToCity()
    }

    private fun buyTreasureMap() {
        gameScreen.text.text = "Вы купили карту сокровищ, она поможет вам найти потайные ходы в подземелье."
        boughtTreasureMap = true
        goToCity()
    }

    private fun buyCompass() {
        gameScreen.text.text = "Вы купили компас, он может помочь вам в ориентировании."
        goToCity()
    }

    private fun acceptMageQuest() {
        gameScreen.text.text = "Вы приняли задание мага, оно может привести к новым приключениям."
        goToCity()
    }

    private fun talkToMercenary() {
        gameScreen.text.text = "Наемник поднимает голову и встречает ваш взгляд. 'Слышал, ты не боишься темных мест,' — произносит он тихим голосом, будто доверяет вам страшную тайну. 'За пределами города, в глубине заброшенного замка, покоится артефакт, способный даровать силу, о которой не смеют говорить. Но его сторожит древнее проклятье, с которым справятся только те, кто готов рискнуть всем. Ты готов? Можешь приобрести зелье на рынке или следовать по карте, но помни: каждый выбор приближает тебя к истине… или к забвению.'"

        gameScreen.button1.text = "Принять задание"
        gameScreen.button2.text = "Отказаться"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener { acceptMercenaryQuest() }
        gameScreen.button2.setOnClickListener { goToTavern() }
    }

    private fun acceptMercenaryQuest() {
        acceptedMercenaryQuest = true
        gameScreen.text.text = "Вы приняли задание наемника. Вам нужно будет отправиться в лес, чтобы помочь ему с бандитами."
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Merc").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться в таверну"
        gameScreen.button1.setOnClickListener { goToTavern() }
    }

    private fun talkToTavernOwner() {
        gameScreen.text.text = "Хозяин таверны рассказывает вам историю о древнем артефакте, который спрятан где-то в подземелье города."
        gameScreen.button1.text = "Спросить о подземелье"
        gameScreen.button2.text = "Вернуться к таверне"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE

        gameScreen.button1.setOnClickListener { askAboutDungeon() }
        gameScreen.button2.setOnClickListener { goToTavern() }
    }

    private fun askAboutDungeon() {
        gameScreen.text.text = "Хозяин таверны рассказывает, что артефакт защищен древними магическими ловушками и только избранный может его добыть."
        gameScreen.button1.text = "Исследовать подземелье"
        gameScreen.button2.text = "Вернуться в таверну"
        gameScreen.image.setImageResource(R.drawable.entrance)
        gameScreen.button1.setOnClickListener { enterDungeon() }
        gameScreen.button2.setOnClickListener { goToTavern() }
    }

    private fun exploreSurroundings() {
        gameScreen.text.text = "Вы осматриваете окрестности и находите скрытую тропинку, ведущую в старое заброшенное подземелье."
        gameScreen.button1.text = "Войти в подземелье"
        gameScreen.button2.text = "Вернуться к воротам города"
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE
        gameScreen.image.setImageResource(R.drawable.entrance)

        gameScreen.button1.setOnClickListener { enterDungeon() }
        gameScreen.button2.setOnClickListener { StartingPoint() }
    }

    private fun enterDungeon() {
        gameScreen.text.text = "Вы входите в мрачное подземелье. Перед вами несколько темных коридоров."
        gameScreen.button1.text = "Пойти налево"
        gameScreen.button2.text = "Пойти направо"
        gameScreen.button3.text = "Осмотреть стены"
        gameScreen.button4.text = "Вернуться"
        gameScreen.button2.visibility = View.VISIBLE
        gameScreen.button3.visibility = View.VISIBLE
        gameScreen.button4.visibility = View.VISIBLE
        gameScreen.button1.isEnabled = true
        gameScreen.button2.isEnabled = true
        gameScreen.image.setImageResource(R.drawable.dungeon)

        gameScreen.button1.setOnClickListener { advanceThroughDungeon("налево") }
        gameScreen.button2.setOnClickListener { advanceThroughDungeon("направо") }
        gameScreen.button3.setOnClickListener { examineDungeonWalls() }
        gameScreen.button4.setOnClickListener { exploreSurroundings() }
    }

    private fun advanceThroughDungeon(direction: String) {
        gameScreen.text.text = "Вы идете $direction и чувствуете магическое присутствие. Впереди светится каменная дверь с рунами."
        gameScreen.button1.text = "Использовать карту (если она у вас есть)"
        gameScreen.button2.text = "Использовать зелье силы (если оно у вас есть)"
        gameScreen.button3.text = "Попробовать открыть дверь"
        gameScreen.button4.text = "Вернуться назад"
        gameScreen.button2.visibility = View.VISIBLE
        gameScreen.button3.visibility = View.VISIBLE
        gameScreen.button4.visibility = View.VISIBLE

        gameScreen.button1.isEnabled = boughtTreasureMap
        gameScreen.button2.isEnabled = boughtPotion

        gameScreen.button1.setOnClickListener { followMap() }
        gameScreen.button2.setOnClickListener { usePotion() }
        gameScreen.button3.setOnClickListener { tryToOpen(direction) }
        gameScreen.button4.setOnClickListener { enterDungeon() }
    }

    private fun tryToOpen(direction: String) {
        gameScreen.text.text = "Вы не смогли открыть двери, видимо стоит найти другое решение"
        gameScreen.button1.text = "Попробовать что-нибудь другое"
        gameScreen.button1.isEnabled=true
        gameScreen.button2.isEnabled=true
        gameScreen.button2.visibility = View.INVISIBLE
        gameScreen.button3.visibility = View.INVISIBLE
        gameScreen.button4.visibility = View.INVISIBLE
        gameScreen.button1.setOnClickListener { enterDungeon() }
    }

    private fun examineDungeonWalls() {
        gameScreen.text.text = "На стенах вы замечаете странные знаки и символы. Возможно, они помогут вам пройти дальше."
        gameScreen.button1.text = "Изучить знаки"
        gameScreen.button2.text = "Вернуться"
        gameScreen.button1.setOnClickListener { readSymbols() }
        gameScreen.button2.setOnClickListener { enterDungeon() }
    }

    private fun readSymbols() {
        gameScreen.text.text = "Знаки на стенах подсказали вам правильный путь. Вы двигаетесь дальше с большей уверенностью."
        gameScreen.button1.text = "Продолжить путь"
        gameScreen.button1.setOnClickListener { advanceThroughDungeon("вперед") }
    }

    private fun followMap() {
        gameScreen.text.text = "Вы следуете за указаниями на карте, тщательно расшифровывая каждую линию и символ. Дорога ведет вас через темные коридоры подземелья, где эхом раздаются ваши шаги. Карта указывает на массивные двери с рунными знаками, и сердце замирает от волнения. Вы чувствуете, что за этими дверями находится то, ради чего вы прошли этот путь, что-то древнее и могущественное, ожидающее лишь тех, кто осмелится приблизиться."

        gameScreen.button1.text = "Войти в комнату сокровищ"
        gameScreen.button1.isEnabled = true
        gameScreen.button2.isEnabled = true
        gameScreen.button1.setOnClickListener { findTreasureRoom() }
    }

    private fun usePotion() {
        gameScreen.text.text = "Вы выпили зелье силы и чувствуете, что сможете открыть даже самые тяжелые двери."
        boughtPotion = false  // Зелье можно использовать только один раз
        gameScreen.button1.isEnabled = true
        gameScreen.button2.isEnabled = true
        openDoors()
    }

    private fun openDoors() {
        gameScreen.text.text = "Вы открываете дверь и находите древний артефакт. Ваша миссия выполнена!"
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Art").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться в город"
        gameScreen.button1.isEnabled = true
        gameScreen.button2.isEnabled = true
        gameScreen.button1.setOnClickListener { goToCity() }
    }

    private fun findTreasureRoom() {
        gameScreen.text.text = "Вы находите сокровища и древние артефакты. Ваше приключение увенчалось успехом!"
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            database.child("Users").child(userId).child("achievements").child("Art").setValue(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Toast.makeText(context, "Достижение сохранено!", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(context, "Ошибка сохранения достижения", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        gameScreen.button1.text = "Вернуться в город"
        gameScreen.button1.setOnClickListener { goToCity() }
    }
}


