package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.util.*


// 随机返回 apiList 中的一个元素
fun Get_WizardLM_LOCAL_API_Randomly(): String {
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
object WizardLMUtil {

    fun GetAPI(): String {
        val API = Get_WizardLM_LOCAL_API_Randomly()
        println("====================================== API:$API ======================================")
        return API
    }


    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}
     */
    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = buildBlogPrompt(prompt)
        data["temperature"] = 0.95
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(600 * 1000)
            .responseString()

        val res = result.get()

        return ParseWizardLMResponse(res)
    }


    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = prompt
        data["temperature"] = 0.95
        data["max_new_tokens"] = 8192
        data["max_context_length"] = 8192
        data["truncation_length"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = GetAPI().httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(600 * 1000)
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

# 1.背景介绍

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

        text = text.replace("    heta","\\theta")
        text = text.replace("    ext","\\text")
        text = text.replace("\nabla","\\nabla")

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
                if (firstLineSpaces >0){
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
