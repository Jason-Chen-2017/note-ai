package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.light.noteai.LLMUtil.buildBlogPrompt
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_QianWen_LOCAL_API_Randomly(): String {
    val API_LIST = listOf(
        "http://127.0.0.1:5000/api/v1/generate",
    )
    val rand = Random()
    val index = rand.nextInt(API_LIST.size) // 0,1,2,3 randomly
    return API_LIST[index]
}


/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object QianWenUtil {

    fun GetAPI(): String {
        val API = Get_QianWen_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }


    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":5120,"max_context_length":5120,"truncation_length":5120}
     */
    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = buildBlogPrompt(prompt)
        data["temperature"] = 0.95
        data["max_new_tokens"] = 5120
        data["max_context_length"] = 5120
        data["truncation_length"] = 5120
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(1200 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(1200 * 1000)
            .timeoutRead(1200 * 1000)
            .responseString()

        val res = result.get()

        return ParseWizardLMResponse(res)
    }


    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = prompt
        data["temperature"] = 0.95
        data["max_new_tokens"] = 5120
        data["max_context_length"] = 5120
        data["truncation_length"] = 5120
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(1200 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(1200 * 1000)
            .timeoutRead(1200 * 1000)
            .responseString()

        val res = result.get()

        return ParseWizardLMResponse(res)
    }


    private fun ParseWizardLMResponse(res: String): String {

        val gson = Gson()
        val WizardLMResponse = gson.fromJson(res, WizardLMResponse::class.java)

        if (WizardLMResponse.results.isNotEmpty()) {
            var text = WizardLMResponse.results[0].text
            println("text=$text")

//            text = replaceNewLineAndTab(text)

            val completeText =
                """
作者：禅与计算机程序设计艺术                    

# 1.简介
  
${text}

"""

            return completeText

        } else {
            return "\n"
        }

    }

    class WizardLMResponse {
        var results: List<TextObject> = listOf()
    }

    class TextObject {
        var text = ""
    }



}
