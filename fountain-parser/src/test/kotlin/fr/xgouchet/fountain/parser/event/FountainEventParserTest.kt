package fr.xgouchet.fountain.parser.event

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.nhaarman.mockito_kotlin.inOrder
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness


@RunWith(Parameterized::class)
class FountainEventParserTest(private val inputPath: String,
                              private val output: Array<LineEvent>) {


    @Rule @JvmField val mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    private lateinit var testedParser: FountainEventParser
    @Mock private lateinit var mockListener: FountainEventParserListener

    @Before
    fun setUp() {
        testedParser = FountainEventParser()
    }


    @Test
    fun parse() {
        // arrange
        val inputStream = FountainEventParserTest::class.java.getResource(inputPath).openStream()

        // act
        testedParser.parse(inputStream, mockListener)

        // assert
        inOrder(mockListener) {
            argumentCaptor<LineEvent>().apply {
                verify(mockListener, atLeastOnce()).lineRead(capture())

                assertThat(allValues)
                        .containsExactly(*output)
            }

            verify(mockListener).scriptEnd()
            verifyNoMoreInteractions()
        }
    }


    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} : ‘{0}’")
        fun testSuiteData(): List<Array<Any>> {
            val list = mutableListOf<Array<Any>>()

            list.testData(
                    "/unit/title_page.fountain",
                    MetadataEvent("Title", "**THE LAST BIRTHDAY CARD**"),
                    MetadataEvent("Credit", "Written by"),
                    MetadataEvent("Author", "Stu Maschwitz"),
                    MetadataEvent("Draft date", "7/8/1998")
            )

            list.testData(
                    "/unit/scenes_with_dot.fountain",
                    SceneHeaderEvent(type = "INT.", description = "TARDIS - UNKNOWN"),
                    SceneHeaderEvent(type = "EXT.", description = "WESTMINSTER ABBEY - NIGHT"),
                    SceneHeaderEvent(type = "EST.", description = "GRAND CANYON – DAY"),
                    SceneHeaderEvent(type = "INT./EXT.", description = "RONNA'S CAR – NIGHT [DRIVING]")
            )

            list.testData(
                    "/unit/scenes_with_space.fountain",
                    SceneHeaderEvent(type = "INT", description = "TARDIS - UNKNOWN"),
                    SceneHeaderEvent(type = "EXT", description = "WESTMINSTER ABBEY - NIGHT"),
                    SceneHeaderEvent(type = "EST", description = "GRAND CANYON – DAY"),
                    SceneHeaderEvent(type = "INT/EXT", description = "DONNA'S CAR – NIGHT [DRIVING]"),
                    SceneHeaderEvent(type = "I/E", description = "THE STAIRS OF THE CHURCH - DAY")
            )

            list.testData(
                    "/unit/scenes_with_numbering.fountain",
                    SceneHeaderEvent(type = "INT", description = "TARDIS - UNKNOWN", numbering = "1"),
                    SceneHeaderEvent(type = "INT/EXT", description = "DONNA'S CAR – NIGHT [DRIVING]", numbering = "2.1"),
                    SceneHeaderEvent(type = "I/E", description = "THE STAIRS OF THE CHURCH - DAY", numbering = "2.2"),
                    SceneHeaderEvent(type = "EXT.", description = "WESTMINSTER ABBEY - NIGHT", numbering = "¯\\_(ツ)_/¯"),
                    SceneHeaderEvent(type = "EST.", description = "GRAND CANYON – DAY", numbering = "I-1-A")
            )

            list.testData(
                    "/unit/dialogs_simple.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    CharacterCueEvent(name = "SCOTT"),
                    DialogEvent("I got another birthday card today."),
                    CharacterCueEvent(name = "BAXTER"),
                    DialogEvent("That's great!"),
                    CharacterCueEvent(name = "SCOTT"),
                    DialogEvent("It is?"),
                    CharacterCueEvent(name = "BAXTER"),
                    DialogEvent("Haven't you been complaining about money lately?")
            )

            list.testData(
                    "/unit/dialogs_multiline.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    CharacterCueEvent(name = "SCOTT"),
                    DialogEvent("I got another birthday card today.\n" +
                            "I can't remember the last time I had so many.\n" +
                            "Like, a long time ago…"),
                    CharacterCueEvent(name = "BAXTER"),
                    DialogEvent("That's great!\n" +
                            "\n" +
                            "With a two space dialog breaker !")
            )

            list.testData(
                    "/unit/dialogs_parenthetical.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    CharacterCueEvent(name = "STEEL"),
                    ParentheticalEvent("starting the engine"),
                    DialogEvent("So much for retirement!"),
                    CharacterCueEvent(name = "JACK"),
                    ParentheticalEvent("in Vietnamese, subtitled"),
                    DialogEvent("*Did you know Brick and Steel are retired?*"),
                    CharacterCueEvent(name = "STEEL"),
                    DialogEvent("They're coming out of the woodwork!"),
                    ParentheticalEvent("pause"),
                    DialogEvent("No, everybody we've put away!"),
                    ParentheticalEvent("pause"),
                    DialogEvent("Point Blank Sniper?")
            )

            list.testData(
                    "/unit/dialogs_extension.fountain",
                    SceneHeaderEvent(type = "INT.", description = "MOM'S HOUSE - DAY"),
                    CharacterCueEvent(name = "MOM", extension = "On screen"),
                    DialogEvent("Luke! Come down for supper!"),
                    CharacterCueEvent(name = "HANS", extension = "on the radio"),
                    DialogEvent("What was it you said?")
            )

            list.testData(
                    "/unit/actions.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    ActionEvent("They drink long and well from the beers."),
                    ActionEvent("And then there's a long beat.\n" +
                            "Longer than is funny.\n" +
                            "Long enough to be depressing."),
                    ActionEvent("The men look at each other.")
            )

            list.testData(
                    "/unit/actions_indented.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    ActionEvent("He opens the card.  A simple little number inside of which is hand written:"),
                    ActionEvent("          Scott --"),
                    ActionEvent("          Jacob Billups\n" +
                            "          Palace Hotel, RM 412\n" +
                            "          1:00 pm tomorrow"),
                    ActionEvent("Scott exasperatedly throws down the card on the table and picks up the phone, hitting speed dial #1...")
            )

            list.testData(
                    "/unit/tricky_blocks.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    CharacterCueEvent(name = "JIM"),
                    DialogEvent("Hey I'm talking, and I might event…\n" +
                            "YELL OUT LOUD\n" +
                            "But it's just a trick guys"),
                    ParentheticalEvent("he sit on the floor"),
                    ActionEvent("A big elephant storms in, and trumpets with its trunk\n" +
                            "BBBWWUUUUUUNNN\n" +
                            "Then it leaves the room")
            )

            list.testData(
                    "/unit/page_breaks.fountain",
                    SceneHeaderEvent(type = "INT.", description = "WHATEVER"),
                    CharacterCueEvent(name = "SCOTT"),
                    DialogEvent("I got another birthday card today."),
                    PageBreakEvent,
                    CharacterCueEvent(name = "BAXTER"),
                    DialogEvent("That's great!"),
                    PageBreakEvent,
                    CharacterCueEvent(name = "SCOTT"),
                    DialogEvent("It is?"),
                    CharacterCueEvent(name = "BAXTER"),
                    DialogEvent("Haven't you been complaining about money lately?"),
                    ActionEvent("He claps and then…\n" +
                            "  \n" +
                            "stops.")
            )

            list.testData(
                    "/unit/transitions.fountain",
                    TransitionEvent("FADE IN TO:"),
                    SceneHeaderEvent(type = "EXT.", description = "GRAND CANYON - DAY"),
                    TransitionEvent("FADE TO BLACK."),
                    TransitionEvent("CUT TO:"),
                    SceneHeaderEvent(type = "INT.", description = "TARDIS - UNKNOWN"),
                    TransitionEvent("WIBBLY WOBBLY TRANSITION TO:"),
                    SceneHeaderEvent(type = "EXT.", description = "SPACE - UNKNOWN"),
                    TransitionEvent("FADE OUT."),
                    TransitionEvent("CUT TO BLACK.")
            )

            list.testData(
                    "/unit/power_user.fountain",
                    TransitionEvent("a weird transition that make every one cry"),
                    SceneHeaderEvent(type = ".", description = "This is a Scene !"),
                    CharacterCueEvent(name = "My Character with ʷᵉⁱʳᵈ characters ?"),
                    DialogEvent("Hello World"),
                    ActionEvent("THIS IS NOT A CHARACTER")
            )

            return list
        }

        private fun MutableList<Array<Any>>.testData(inputPath: String, vararg lines: LineEvent) {
            add(
                    arrayOf(
                            inputPath,
                            arrayOf(*lines)
                    )
            )
        }
    }
}

/*
✓ Scene Heading
✓  Scene Heading POWER USER .
✓ Scene Heading Numbering
✓ Action
✓ Action POWER USER !
✓ Character
✓ Character @
✓ Dialogue
✓ Parenthetical
TODO Dual Dialogue
TODO Lyrics ~
✓ Transition
✓ Transition POWER USER >
TODO Centered Text > <
✓ Title Page
✓ Page Breaks
✓ Punctuation
✓ Line Breaks
✓ Indenting
TODO Notes [[ … ]]
TODO Boneyard /★ … ★/
TODO Sections and Synopses # / =
TODO Emphasis
*/
