package dev.johnoreilly.starwars.shared

import GetAllFilmsQuery
import GetAllPeopleQuery
import com.apollographql.apollo3.ApolloClient
import dev.johnoreilly.starwars.shared.model.Film
import dev.johnoreilly.starwars.shared.model.Person
import dev.johnoreilly.starwars.shared.model.mapToModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class StarWarsRepository {
    private val serverUrl = "https://swapi-graphql.netlify.app/.netlify/functions/index"
    //private val serverUrl = "http://10.0.2.2:8080/graphql"
    private val apolloClient = ApolloClient(serverUrl)

    suspend fun getPeople(): List<Person> {
        val response = apolloClient.query(GetAllPeopleQuery())
        return response.dataOrThrow.allPeople.people.mapNotNull { it?.personFragment?.mapToModel() }
    }

    suspend fun getFilms(): List<Film> {
        val response = apolloClient.query(GetAllFilmsQuery())
        return response.dataOrThrow.allFilms.films.mapNotNull { it?.filmFragment?.mapToModel() }
    }


    // called from iOS
    private val scope = MainScope()

    fun getPeople(success: (List<Person>) -> Unit)  {
        scope.launch {
            success(getPeople())
        }
    }

    fun getFilms(success: (List<Film>) -> Unit)  {
        scope.launch {
            success(getFilms())
        }
    }
}