package cli.hello

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CLIHello : CommandLineRunner {
    // CommandLineRunnerインターフェースのRunメソッドは、
    // アプリケーションコンテキストがロードされた後で１度だけ呼ばれる。
    override fun run(vararg args: String?) {
        println("Hello world!")
    }
}

fun main(args: Array<String>) {
    runApplication<CLIHello>(*args)
}