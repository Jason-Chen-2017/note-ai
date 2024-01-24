package com.light.noteai


import java.util.*


enum class LLM_MODEL {
    DeepSeek_MODEL,
    Open_Chat_MODEL,
    Yi_Chat_MODEL,
    NeuralBeagle_MODEL,
    QWen_MODEL,
    ChatGLM_MODEL,
    WizardLM_MODEL
}

// 配置使用的模型
val USE_LLM_MODEL = LLM_MODEL.Open_Chat_MODEL
//val USE_LLM_MODEL = LLM_MODEL.DeepSeek_MODEL
//val USE_LLM_MODEL = LLM_MODEL.NeuralBeagle_MODEL
//val USE_LLM_MODEL = LLM_MODEL.Yi_Chat_MODEL
//val USE_LLM_MODEL = LLM_MODEL.ChatGLM_MODEL
//val USE_LLM_MODEL = LLM_MODEL.WizardLM_MODEL

object LLMUtil {

    /*
    {"prompt":"现在你是一位人工智能专家,程序员,软件架构师,CTO，请以《了解现代编程语言和框架：Java、Kotlin和React Native》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 文章核心内容包含：1.背景介绍，2.文章目的，3.目标受众，4.基本概念术语说明，5.核心算法原理数学公式和具体操作步骤，6.代码实例和解释说明，7.未来发展趋势与挑战，8.附录常见问题与解答这8大部分。文章字数大于 8000 字。使用markdown格式。标题：了解现代编程语言和框架：Java、Kotlin和React Native",
    "max_new_tokens":8192,"max_context_length":8192,"truncation_length":8192}
     */
    fun WriteBlog(prompt: String): String {
        return if (USE_LLM_MODEL == LLM_MODEL.QWen_MODEL) {
            QianWenUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.WizardLM_MODEL) {
            WizardLMUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.ChatGLM_MODEL) {
            ChatGLMUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Open_Chat_MODEL) {
            OpenChatUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.DeepSeek_MODEL) {
            DeepSeekUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Yi_Chat_MODEL) {
            YiUtil.WriteBlog(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.NeuralBeagle_MODEL) {
            NeuralBeagleLLM.WriteBlog(prompt)
        } else {
            "No LLM"
        }
    }

    fun Complete(prompt: String): String {
        return if (USE_LLM_MODEL == LLM_MODEL.QWen_MODEL) {
            QianWenUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.WizardLM_MODEL) {
            WizardLMUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.ChatGLM_MODEL) {
            ChatGLMUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Open_Chat_MODEL) {
            OpenChatUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.DeepSeek_MODEL) {
            DeepSeekUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.Yi_Chat_MODEL) {
            YiUtil.Complete(prompt)
        } else if (USE_LLM_MODEL == LLM_MODEL.NeuralBeagle_MODEL) {
            NeuralBeagleLLM.Complete(prompt)
        } else {
            "No LLM"
        }
    }



    fun buildBlogPrompt(prompt: String): String {
        val fullPrompt = """
您是一位世界级人工智能专家,程序员,软件架构师,CTO,世界顶级技术畅销书作者，计算机图灵奖获得者，计算机领域大师。
现在请您以逻辑清晰、结构紧凑、简单易懂的专业的技术语言（章节标题要非常吸引读者），写一篇专业IT领域的技术博客文章。
具体要求如下：

GOALS:

请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客文章。
文章核心章节内容必须包含：

# 1.背景介绍
# 2.核心概念与联系
# 3.核心算法原理和具体操作步骤以及数学模型公式详细讲解
# 4.具体最佳实践：代码实例和详细解释说明
# 5.实际应用场景
# 6.工具和资源推荐
# 7.总结：未来发展趋势与挑战
# 8.附录：常见问题与解答

这6大部分。各个一级章节的子目录请具体细化到三级目录.

CONSTRAINTS:
1.文章字数大于8000字
2.文章内容使用markdown格式
3.数学模型公式请使用latex格式，latex嵌入文中独立段落使用 $$，段落内使用 $
4.文章末尾不要列出参考文献
5.深入研究和准确性：在撰写博客之前，进行充分的研究并确保你对所涉及的技术有深入的了解。提供准确的信息和数据，以增加你的博客的可信度。
6.尽量使用简明扼要的语言来解释技术概念，并提供实际示例帮助读者理解。
7.提供实用价值：确保你的博客提供实用的价值，例如解决问题的方法、最佳实践、技巧和技术洞察。读者更倾向于寻找能够帮助他们解决问题或提升技能的内容。
8.清晰明了的结构：使用清晰的文章结构，例如引言、背景知识、主要内容和结论。这样读者可以更容易地跟随你的思路和理解文章。

下面我们开始写文章的全部内容：

# ${prompt}

""".trimIndent()

        println(Date())
        println(fullPrompt)

        return fullPrompt
    }



}

