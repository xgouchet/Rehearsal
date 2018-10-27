package fr.xgouchet.fountain.parser.dom

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness


@RunWith(Parameterized::class)
class FountainDomParserTest(private val inputPath: String,
                            private val output: Script) {

    @Rule @JvmField val mockito = MockitoJUnit.rule().strictness(Strictness.LENIENT)

    private lateinit var testedParser: FountainDomParser

    @Before
    fun setUp() {
        testedParser = FountainDomParser()
    }

    @Test
    fun parse() {
        // arrange
        val inputStream = FountainDomParserTest::class.java.getResource(inputPath).openStream()

        // act
        val result = testedParser.parse(inputStream)

        // assert
        assertThat(result)
                .isEqualTo(output)
    }


    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} : ‘{0}’")
        fun testSuiteData(): List<Array<Any>> {
            val list = mutableListOf<Array<Any>>()

            list.add(arrayOf(
                    "/unit/title_page.fountain",
                    Script.Builder().apply {
                        titlePage = TitlePage.Builder().apply {
                            title = "**THE LAST BIRTHDAY CARD**"
                            addMetadata("Credit", "Written by")
                            addMetadata("Author", "Stu Maschwitz")
                            addMetadata("Draft date", "7/8/1998")
                        }
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/scenes_with_dot.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "TARDIS - UNKNOWN"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EXT."
                            header.description = "WESTMINSTER ABBEY - NIGHT"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EST."
                            header.description = "GRAND CANYON – DAY"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "INT./EXT."
                            header.description = "RONNA'S CAR – NIGHT [DRIVING]"
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/scenes_with_space.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT"
                            header.description = "TARDIS - UNKNOWN"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EXT"
                            header.description = "WESTMINSTER ABBEY - NIGHT"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EST"
                            header.description = "GRAND CANYON – DAY"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "INT/EXT"
                            header.description = "DONNA'S CAR – NIGHT [DRIVING]"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "I/E"
                            header.description = "THE STAIRS OF THE CHURCH - DAY"
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/scenes_with_numbering.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT"
                            header.description = "TARDIS - UNKNOWN"
                            header.numbering = "1"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "INT/EXT"
                            header.description = "DONNA'S CAR – NIGHT [DRIVING]"
                            header.numbering = "2.1"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "I/E"
                            header.description = "THE STAIRS OF THE CHURCH - DAY"
                            header.numbering = "2.2"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EXT."
                            header.description = "WESTMINSTER ABBEY - NIGHT"
                            header.numbering = "¯\\_(ツ)_/¯"
                        })
                        addPart(Scene.Builder().apply {
                            header.type = "EST."
                            header.description = "GRAND CANYON – DAY"
                            header.numbering = "I-1-A"
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/dialogs_simple.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "SCOTT"
                                addDialog("I got another birthday card today.")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "BAXTER"
                                addDialog("That's great!")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "SCOTT"
                                addDialog("It is?")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "BAXTER"
                                addDialog("Haven't you been complaining about money lately?")
                            })
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/dialogs_multiline.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "SCOTT"
                                addDialog("I got another birthday card today.\n" +
                                        "I can't remember the last time I had so many.\n" +
                                        "Like, a long time ago…")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "BAXTER"
                                addDialog("That's great!\n" +
                                        "\n" +
                                        "With a two space dialog breaker !")
                            })
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/dialogs_parenthetical.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "STEEL"
                                addParenthetical("starting the engine")
                                addDialog("So much for retirement!")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "JACK"
                                addParenthetical("in Vietnamese, subtitled")
                                addDialog("*Did you know Brick and Steel are retired?*")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "STEEL"
                                addDialog("They're coming out of the woodwork!")
                                addParenthetical("pause")
                                addDialog("No, everybody we've put away!")
                                addParenthetical("pause")
                                addDialog("Point Blank Sniper?")
                            })
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/dialogs_extension.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "MOM'S HOUSE - DAY"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "MOM"
                                characterExtension = "On screen"
                                addDialog("Luke! Come down for supper!")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "HANS"
                                characterExtension = "on the radio"
                                addDialog("What was it you said?")
                            })
                        })
                    }.build()
            ))


            list.add(arrayOf(
                    "/unit/actions.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(ActionCue.Builder().apply {
                                direction = "They drink long and well from the beers."
                            })
                            addCue(ActionCue.Builder().apply {
                                direction = "And then there's a long beat.\n" +
                                        "Longer than is funny.\n" +
                                        "Long enough to be depressing."
                            })
                            addCue(ActionCue.Builder().apply {
                                direction = "The men look at each other."
                            })
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/actions_indented.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(ActionCue.Builder().apply {
                                direction = "He opens the card.  A simple little number inside of which is hand written:"
                            })

                            addCue(ActionCue.Builder().apply {
                                direction = "          Scott --"
                            })
                            addCue(ActionCue.Builder().apply {
                                direction = "          Jacob Billups\n" +
                                        "          Palace Hotel, RM 412\n" +
                                        "          1:00 pm tomorrow"
                            })
                            addCue(ActionCue.Builder().apply {
                                direction = "Scott exasperatedly throws down the card on the table and picks up the phone, hitting speed dial #1..."
                            })
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/tricky_blocks.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "JIM"

                                addDialog("Hey I'm talking, and I might event…\n" +
                                        "YELL OUT LOUD\n" +
                                        "But it's just a trick guys")
                                addParenthetical("he sit on the floor")
                            })

                            addCue(ActionCue.Builder("A big elephant storms in, and trumpets with its trunk\n" +
                                    "BBBWWUUUUUUNNN\n" +
                                    "Then it leaves the room"))
                        })
                    }.build()
            ))

            list.add(arrayOf(
                    "/unit/page_breaks.fountain",
                    Script.Builder().apply {
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "WHATEVER"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "SCOTT"
                                addDialog("I got another birthday card today.")
                            })
                            addCue(PageBreakCue.Builder)
                            addCue(CharacterCue.Builder().apply {
                                characterName = "BAXTER"
                                addDialog("That's great!")
                            })
                            addCue(PageBreakCue.Builder)
                            addCue(CharacterCue.Builder().apply {
                                characterName = "SCOTT"
                                addDialog("It is?")
                            })
                            addCue(CharacterCue.Builder().apply {
                                characterName = "BAXTER"
                                addDialog("Haven't you been complaining about money lately?")
                            })
                            addCue(ActionCue.Builder("He claps and then…\n" +
                                    "  \n" +
                                    "stops."))
                        })
                    }.build()

            ))

            list.add(arrayOf(
                    "/unit/transitions.fountain",
                    Script.Builder().apply {
                        addPart(Transition.Builder("FADE IN TO:"))
                        addPart(Scene.Builder().apply {
                            header.type = "EXT."
                            header.description = "GRAND CANYON - DAY"
                        })
                        addPart(Transition.Builder("FADE TO BLACK."))
                        addPart(Transition.Builder("CUT TO:"))
                        addPart(Scene.Builder().apply {
                            header.type = "INT."
                            header.description = "TARDIS - UNKNOWN"
                        })
                        addPart(Transition.Builder("WIBBLY WOBBLY TRANSITION TO:"))
                        addPart(Scene.Builder().apply {
                            header.type = "EXT."
                            header.description = "SPACE - UNKNOWN"
                        })
                        addPart(Transition.Builder("FADE OUT."))
                        addPart(Transition.Builder("CUT TO BLACK."))
                    }.build()
            ))



            list.add(arrayOf(
                    "/unit/power_user.fountain",
                    Script.Builder().apply {
                        addPart(Transition.Builder("a weird transition that make every one cry"))
                        addPart(Scene.Builder().apply {
                            header.type = "."
                            header.description = "This is a Scene !"

                            addCue(CharacterCue.Builder().apply {
                                characterName = "My Character with ʷᵉⁱʳᵈ characters ?"

                                addDialog("Hello World")
                            })

                            addCue(ActionCue.Builder("THIS IS NOT A CHARACTER"))
                        })
                    }.build()
            ))

            list.add(
                    arrayOf(
                            "/unit/lyrics.fountain",
                            Script.Builder().apply {
                                addPart(Scene.Builder().apply {
                                    header.type = "INT."
                                    header.description = "WHATEVER"

                                    addCue(LyricsCue.Builder("Willy Wonka! Willy Wonka! The amazing chocolatier!\n" +
                                            "Willy Wonka! Willy Wonka! Everybody give a cheer!"))
                                })
                            }.build()
                    )
            )


            // For now, sections and synopsis are not handled in DOM
            // This can be problematic, but as they can happen anywhere in the middle of scenes or outside of them,
            // I'm not sure where to store them in the hierarchy
            // TODO (maybe?) create an automatic opening scene and only have sections / synopsis as a scene "cue" element…
            list.add(
                    arrayOf(
                            "/unit/sections_synopsis.fountain",
                            Script.Builder().apply {
                                addPart(Transition.Builder("HERE WE GO:"))
                                addPart(Scene.Builder().apply {
                                    header.type = "INT."
                                    header.description = "SAN FRANCISCO APARTMENT, DAY"
                                })
                            }.build()
                    )
            )

            return list
        }

    }

}

/*
TODO Dual Dialog
TODO Centered Text > <
TODO Sections and Synopses # / =
 */
