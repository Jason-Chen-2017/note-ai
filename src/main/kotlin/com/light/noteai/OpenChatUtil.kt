package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_OpenChat_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://localhost:18888/v1/chat/completions",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}


/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object OpenChatUtil {

    fun GetAPI(): String {
        val API = Get_OpenChat_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }


    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}
     */
    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["model"] = "openchat_3.5"
        data["condition"] = "Code"
        data["top_p"] = 0.9
        data["temperature"] = 0.1

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", buildBlogPrompt(prompt))))
        data["messages"] = messages

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        println(res)

        return ParseOpenChatResponse(res)
    }


    /**
     * Input:
     * {
     *     "model": "openchat_3.5",
     *     "condition": "Code",
     *     "messages": [{"role": "user", "content": "1+1="}]
     *   }
     *
     * Output:
     *   {
     *   "id": "cmpl-bafc983ab99848c1b32a83cd35181377",
     *   "object": "chat.completion",
     *   "created": 1701184195,
     *   "model": "openchat_3.5",
     *   "choices": [
     *     {
     *       "index": 0,
     *       "message": {
     *         "role": "assistant",
     *         "content": "2"
     *       },
     *       "finish_reason": "stop"
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 13,
     *     "total_tokens": 16,
     *     "completion_tokens": 3
     *   }
     * }
     */
    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["model"] = "openchat_3.5"
        data["condition"] = "Code"
        data["top_p"] = 0.9
        data["temperature"] = 0.1

        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", prompt)))
        data["messages"] = messages

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        return ParseOpenChatResponse(res)
    }


    private fun ParseOpenChatResponse(res: String): String {

        val gson = Gson()
        val OpenChatResponse = gson.fromJson(res, OpenChatResponse::class.java)

        if (OpenChatResponse.choices.isNotEmpty()) {
            val text = OpenChatResponse.choices[0].message.content
            println("text=$text")

            val completeText =
                """                 

# 1.背景介绍

${text}

"""

            return completeText

        } else {
            return "\n"
        }

    }


    /**
     * {
     *   "id": "cmpl-bafc983ab99848c1b32a83cd35181377",
     *   "object": "chat.completion",
     *   "created": 1701184195,
     *   "model": "openchat_3.5",
     *   "choices": [
     *     {
     *       "index": 0,
     *       "message": {
     *         "role": "assistant",
     *         "content": "2"
     *       },
     *       "finish_reason": "stop"
     *     }
     *   ],
     *   "usage": {
     *     "prompt_tokens": 13,
     *     "total_tokens": 16,
     *     "completion_tokens": 3
     *   }
     * }
     */
    class OpenChatResponse {
        var id = "cmpl-bafc983ab99848c1b32a83cd35181377"
        var model = "openchat_3.5"
        var choices: List<Choices> = listOf()
    }

    class Choices {
        var index = 0
        var message = Message()
    }

    class Message {
        var role = "assistant"
        var content = ""
    }

    private fun buildBlogPrompt(prompt: String): String {
        val InputPrompt = """
现在你是一位资深技术专家,程序员和软件系统架构师,CTO，请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客文章。
文章核心内容必须包含：
# 1.背景介绍
# 2.核心概念与联系
# 3.核心算法原理和具体操作步骤以及数学模型公式详细讲解
# 4.具体代码实例和详细解释说明
# 5.未来发展趋势与挑战
# 6.附录常见问题与解答
这6大部分。文章字数大于8000字。使用markdown格式。下面我们开始写文章的全部内容。
# ${prompt}
# 1.背景介绍
""".trimIndent()

        println(InputPrompt)

        return InputPrompt
    }


    fun replaceNewLineAndTab(text: String): String {
        // 代码中的换行 制表符的处理
        var txt = text
        txt = txt.replace(
            "\n",
            """
"""
        )

        txt = txt.replace("\t", """    """)
        // fix:  heta --->\theta
        txt = fixLatex(txt)

        // 去掉所有的行首空格数=第一行空格数量
        txt = trimHeadSpaces(txt)

        return txt
    }

    fun fixLatex(txt: String): String {
        var text = txt

        text = text.replace("    heta", "\\theta")
        text = text.replace("    ext", "\\text")
        text = text.replace("\nabla", "\\nabla")

        return text
    }


    /*
    去掉所有的行首空格数=首次遇到开头有空格行的数量
    例如：txt=
    123
    456
      789
      666
    处理之后：
    123
    456
    789
    666
     */
    fun trimHeadSpaces(txt: String): String {
        val lines = txt.lines() // 将文本按行分割成列表

        val firstLineSpaces = lines.indexOfFirst { it.trim().isNotEmpty() && it[0] == ' ' } // 获取首次遇到开头有空格行的数量

        val trimmedLines = lines.mapIndexed { index, line ->
            if (index < firstLineSpaces) {
                line // 在首次遇到开头有空格行之前的行不做处理
            } else {
                if (firstLineSpaces > 0) {
                    val trimmedSpaces = line.takeWhile { it == ' ' }.take(firstLineSpaces) // 获取指定数量的空格
                    line.removePrefix(trimmedSpaces) // 去除指定数量的空格
                } else {
                    line
                }
            }
        }

        return trimmedLines.joinToString("\n") // 将处理后的行重新组合成文本
    }

}
