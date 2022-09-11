package demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

// ----
// Entity
// ----
// Blogの記事
data class Article(
    val id: String?,
    val publishedAt: ZonedDateTime,
    val head: String,
    val description: String,
    val author: User,
)

data class User(
    val id: String,
)

// ----
// API Response
// ----
data class ResponseList(
    val results: List<Article>,
)

// ----
// Database
// ----
@Table("ARTICLES")
data class DBModelArticle(
    @Id val id: String?,
    // val publishedAt: ZonedDateTime,
    val head: String,
    val description: String,
    // val author: Author,
)

interface TableArticle : CrudRepository<DBModelArticle, String> {
    @Query("select * from articles")
    fun findArticles(): List<DBModelArticle>
}

@Service
class RepositoryBlog(
    val tableArticle: TableArticle,
) {
    fun findArticles(): List<DBModelArticle> = tableArticle.findArticles()
    fun post(article: Article) {
        val v = DBModelArticle(
            id = article.id,
            // publishedAt = article.publishedAt,
            head = article.head,
            description = article.description,
            // author = article.author,
        )
        tableArticle.save(v)
    }
}

@RestController
class ArticleResource(
    val repositoryBlog: RepositoryBlog,
) {

    @PostMapping("/articles")
    fun postArticles(
        @RequestBody article: Article,
    ): List<Article> {
        repositoryBlog.post(article)
        return listOf(
            Article(
                id = "3",
                head = "head",
                publishedAt = ZonedDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = ZonedDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = ZonedDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = ZonedDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
        )
    }

    @GetMapping("/articles")
    fun getArticles(
        @RequestParam(
            value = "since",
            defaultValue = "1990-01-01T00:00:00.000Z",
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        sinceNotNull: ZonedDateTime,
        @RequestParam(
            value = "until",
            defaultValue = ""
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        until: ZonedDateTime?,
        @RequestParam(
            value = "tags",
            defaultValue = ""
        )
        tags: Array<String>,
    ): ResponseList {
        val untilNotNull: ZonedDateTime = until ?: ZonedDateTime.now()
        println("since $sinceNotNull")
        println("until $untilNotNull")
        for (v in tags) {
            println("tag13 $v")
        }
        return ResponseList(
            results = listOf(
                Article(
                    id = "1",
                    head = "head",
                    publishedAt = ZonedDateTime.now(),
                    description = "desc",
                    author = User(id = "hoge"),
                ),
                Article(
                    id = "1",
                    head = "head",
                    publishedAt = ZonedDateTime.now(),
                    description = "desc",
                    author = User(id = "hoge"),
                ),
                Article(
                    id = "1",
                    head = "head",
                    publishedAt = ZonedDateTime.now(),
                    description = "desc",
                    author = User(id = "hoge"),
                ),
                Article(
                    id = "1",
                    head = "head",
                    publishedAt = ZonedDateTime.now(),
                    description = "desc",
                    author = User(id = "hoge"),
                ),
            )
        )
    }
}

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
