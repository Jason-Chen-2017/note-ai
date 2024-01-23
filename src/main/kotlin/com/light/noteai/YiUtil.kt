package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.light.noteai.LLMUtil.buildBlogPrompt
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_Yi_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://127.0.0.1:5000/v1/chat/completions",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}


/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object YiUtil {

    fun GetAPI(): String {
        val API = Get_Yi_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }


    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}
     */
    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", buildBlogPrompt(prompt))))
        data["messages"] = messages
        data["temperature"] = 0.95
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val jsonBody = Gson().toJson(data).toString()
        println("request json:${jsonBody}")

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(1200 * 1000)
            .jsonBody(jsonBody)
            .timeout(1200 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()



        val res = result.get()

        return ParseYiResponse(res)
    }


    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        val messages = listOf(mapOf(Pair("role", "user"), Pair("content", prompt)))
        data["messages"] = messages
        data["temperature"] = 0.95
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val jsonBody = Gson().toJson(data).toString()
        println("request json:${jsonBody}")

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(1200 * 1000)
            .jsonBody(jsonBody)
            .timeout(1200 * 1000)
            .timeoutRead(1800 * 1000)
            .responseString()

        val res = result.get()

        return ParseYiResponseComplete(res)
    }



    private fun ParseYiResponse(res: String): String {

        val gson = Gson()
        val YiResponse = gson.fromJson(res, YiResponse::class.java)

        if (YiResponse.choices.isNotEmpty()) {
            val text = YiResponse.choices[0].message.content
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



    private fun ParseYiResponseComplete(res: String): String {

        val gson = Gson()
        val YiResponse = gson.fromJson(res, YiResponse::class.java)

        if (YiResponse.choices.isNotEmpty()) {
            val text = YiResponse.choices[0].message.content
            println("text=$text")
            return text
        } else {
            return "\n"
        }
    }


    /**
     * {
     *   "id": "cmpl-bafc983ab99848c1b32a83cd35181377",
     *   "object": "chat.completion",
     *   "created": 1701184195,
     *   "model": "Yi-34B-Chat-8bits",
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
    class YiResponse {
        var id = "cmpl-bafc983ab99848c1b32a83cd35181377"
        var model = "Yi-34B-Chat-8bits"
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



}
