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
import java.time.OffsetDateTime

// ----
// Entity
// ----
// Blogの記事
data class Article(
    val id: String,
    val publishedAt: OffsetDateTime?,
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
@Table("articles")
data class DBModelArticle(
    @Id val id: String,
    val head: String,
    val description: String,
    val publishedAt: OffsetDateTime?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val deletedAt: OffsetDateTime?
    // val author: Author,
)

@Table("users")
data class DBModelUser(
    @Id val id: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val deletedAt: OffsetDateTime?
)

interface TableArticle : CrudRepository<DBModelArticle, String> {
    @Query("select * from articles")
    fun findArticles(): List<DBModelArticle>
}

@Service
class RepositoryBlog(
    val tableArticle: TableArticle,
) {
    fun findArticles(): List<Article> {
        return tableArticle.findArticles().map {
            println(it)
            Article(
                id = it.id,
                head = it.head,
                description = it.description,
                publishedAt = it.publishedAt,
                author = User("u1"),
            )
        }
    }

    fun post(article: Article) {
        val v = DBModelArticle(
            id = article.id,
            head = article.head,
            description = article.description,
            // author = article.author,
            publishedAt = null,
            updatedAt = null,
            deletedAt = null,
            createdAt = OffsetDateTime.now(),
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
                publishedAt = OffsetDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = OffsetDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = OffsetDateTime.now(),
                description = "desc",
                author = User(id = "hoge"),
            ),
            Article(
                id = "1",
                head = "head",
                publishedAt = OffsetDateTime.now(),
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
        sinceNotNull: OffsetDateTime,
        @RequestParam(
            value = "until",
            defaultValue = ""
        )
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        until: OffsetDateTime?,
        @RequestParam(
            value = "tags",
            defaultValue = ""
        )
        tags: Array<String>,
    ): ResponseList {
        val untilNotNull: OffsetDateTime = until ?: OffsetDateTime.now()
        val articles = repositoryBlog.findArticles()
        return ResponseList(results = articles)
    }
}

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
