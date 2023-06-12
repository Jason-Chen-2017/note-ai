package com.light.noteai


import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson


//fun main() {
//    val title = "大型语言模型的实现技术原理与应用"
//    ChatGLMUtil.WriteBlog(title)
//}

val CHATGLM_LOCAL_API = "http://127.0.0.1:8000"

/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object ChatGLMUtil {

    fun WriteBlog(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = buildBlogPrompt(prompt)
        data["temperature"] = 0.95
        data["max_tokens"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = CHATGLM_LOCAL_API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(600 * 1000)
            .responseString()

        val res = result.get()

        return ParseChatGLMResponse(res, true)
    }


    fun Complete(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = buildLongerPrompt(prompt)
        data["temperature"] = 0.95
        data["max_tokens"] = 8192
        data["top_p"] = 0.9

        val (_, _, result) = CHATGLM_LOCAL_API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(600 * 1000)
            .responseString()

        val res = result.get()

        return ParseChatGLMResponse(res, false)
    }


    private fun ParseChatGLMResponse(res: String, needToc: Boolean): String {

        val gson = Gson()
        val GLMResponse = gson.fromJson(res, GLMResponse::class.java)

        if (GLMResponse.response.isNotEmpty()) {
            var text = GLMResponse.response
            println("text=$text")

            text = replaceNewLineAndTab(text)

            val tocText =
                """
[toc]                    
                
                
${text}

"""

            val completeText = text

            return if (needToc)
                tocText
            else completeText

        } else {
            return "\n"
        }

    }

    class GLMResponse {
        var response: String = ""
        var history: List<List<String>> = listOf()
        var status: Int = 0
        var time: String = "2023-06-11 00:55:14"
    }

    private fun buildBlogPrompt(prompt: String): String {
        val InputPrompt = """
         你是一位人工智能专家,程序员,软件架构师,CTO，请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 字数要求不少于5000字。文章目录如下：

## 1. 引言

- 1.1. 背景介绍
- 1.2. 文章目的
- 1.3. 目标受众

## 2. 技术原理及概念

- 2.1. 基本概念解释
- 2.2. 技术原理介绍
- 2.3. 相关技术比较

## 3. 实现步骤与流程

- 3.1. 准备工作：环境配置与依赖安装
- 3.2. 核心模块实现
- 3.3. 集成与测试

## 4. 示例与应用

- 4.1. 实例分析
- 4.2. 核心代码实现
- 4.3. 代码讲解说明
- 4.4. 应用场景介绍

## 5. 优化与改进

- 5.1. 性能优化
- 5.2. 可扩展性改进
- 5.3. 安全性加固

## 6. 结论与展望

- 6.1. 技术总结
- 6.2. 未来发展趋势与挑战

## 7. 附录：常见问题与解答


请保持逻辑清晰、结构紧凑，以便读者更容易理解和掌握所讲述的技术知识。
             
         """.trimIndent()

        println(InputPrompt)

        return InputPrompt
    }


    private fun buildLongerPrompt(prompt: String): String {
        val InputPrompt = """
         你是一位人工智能专家,程序员,软件架构师,CTO，请以逻辑清晰、结构紧凑、简单易懂的专业的技术语言，并且不少于1000字的内容，续写以下内容：
         ${prompt}
         """.trimIndent()

        println(InputPrompt)

        return InputPrompt
    }


    fun replaceNewLineAndTab(text: String): String {
        // 代码中的换行 制表符的处理
        var txt = text
        txt = txt.replace(
            "\\n",
            """
"""
        )

        txt = txt.replace("\\t", """    """)
        return txt
    }
}
