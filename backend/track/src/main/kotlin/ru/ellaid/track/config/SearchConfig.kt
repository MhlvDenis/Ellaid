package ru.ellaid.track.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.ellaid.track.algorithm.SearchAlgorithm
import ru.ellaid.track.algorithm.SearchWithTyposAlgorithm

@Configuration
class SearchConfig {

    @Bean
    fun searchWithTyposAlgorithm(
        @Value("\${search.return-limit: 5}")
        returnLimit: Long
    ): SearchAlgorithm = SearchWithTyposAlgorithm(returnLimit)
}