package ru.ellaid.track.search

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.ellaid.track.data.entity.Track

class SearchWithTyposAlgorithmTest {

    private val t1 = Track("Throne", "Bring Me The Horizon", "xaxaxa", "xoxoxo")
    private val t2 = Track("Bring Me The Horizon", "Слава КПСС", "xexexe", "xixixi")
    private val t3 = Track("Event Horizon", "Wildways", "xy...", "xz")
    private val t4 = Track("Горизонт", "Владимир Высоцкий", "xrrrr", "krya")

    @Test
    fun exactMatch() {
        val tracks = listOf(t1, t2, t3, t4)

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(2)
                .perform(tracks, "bring me the horizon").toSet(),
            setOf(t1, t2)
        )

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(3)
                .perform(tracks, "horizon").toSet(),
            setOf(t1, t2, t3)
        )
    }

    @Test
    fun searchWithTypos() {
        val tracks = listOf(t1, t2, t3, t4)

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(1)
                .perform(tracks, "brin me the horzon thron").toSet(),
            setOf(t1)
        )

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(3)
                .perform(tracks, "brank the horzon").toSet(),
            setOf(t1, t2, t3)
        )

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(1)
                .perform(tracks, "высотский").toSet(),
            setOf(t4)
        )
    }

    @Test
    fun nameAndAuthor() {
        val tracks = listOf(t1, t2, t3, t4)

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(1)
                .perform(tracks, "bring me the horizon throne").toSet(),
            setOf(t1)
        )

        Assertions.assertEquals(
            SearchWithTyposAlgorithm(1)
                .perform(tracks, "throne bring me the horizon").toSet(),
            setOf(t1)
        )
    }
}