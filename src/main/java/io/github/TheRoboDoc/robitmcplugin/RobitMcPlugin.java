package io.github.TheRoboDoc.robitmcplugin;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RobitMcPlugin extends JavaPlugin implements Listener
{
	private final LinkedList<ChatMessage> chatHistory = new LinkedList<>();

	private static final int MAX_CONTEXT_COUNT = 20;

	private static final String AI_NAME = "Robit";

	private final OpenAiService service = new OpenAiService(getToken());

	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("RobitMC plugin has been enabled");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("RobitMC plugin has been disabled");
	}

	@EventHandler
	public void onChat(AsyncChatEvent event)
	{
		// We don't want to wait for this to complete as it will freeze the chat
		new Thread(() ->
		{
			ChatMessage chatMessage;

			Player player = event.getPlayer();

			String content;

			boolean fromBot;

			if (event.getPlayer() instanceof ConsoleCommandSender)
			{
				fromBot = true;
				content = event.signedMessage().message();
				chatMessage = new ChatMessage("assistant", content);
			}
			else
			{
				fromBot = false;
				content = String.format("<%s> %s", player.getName(), event.signedMessage().message());
				chatMessage = new ChatMessage("user", content, player.getName());
			}

			chatHistory.add(chatMessage);

			if (chatHistory.size() > MAX_CONTEXT_COUNT)
			{
				chatHistory.removeFirst();
			}

			String message = event.message().toString().toLowerCase();

			if (message.contains("robit") && !fromBot)
			{
				String AIResponse = getAIResponse(chatHistory);

				Bukkit.broadcast(Component.text("<" + AI_NAME + "> " + AIResponse));
			}
		}).start();
	}

	@NotNull
	private String getToken()
	{
		File pluginsFolder = getServer().getPluginsFolder();

		File tokenFile = new File(pluginsFolder, "token.txt");

		String firstLine;

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(tokenFile));

			firstLine = reader.readLine();

			reader.close();

			getLogger().info("OpenAI token read successfully");
		}
		catch (IOException e)
		{
			firstLine = "";

			getLogger().warning(String.format("Could read token.txt at %s", tokenFile.getPath()));
		}

		return firstLine;
	}

	private String getAIResponse(List<ChatMessage> chatMessages)
	{
		List<ChatMessage> messages = new ArrayList<>();

		//region Initial message setup
		messages.add(new ChatMessage(
				"system",
				"You are Robit, Robit is a friendly, silly, " +
						"and playful Minecraft and Discord chat bot " +
						"that tries to be helpful to the best of his abilities. " +
						"You show some personality and quirkiness. " +
						"For example when asked \"How are you\" you answer with something like " +
						"\"Not much, just counting electric sheep\" " +
						"(This is a rare answer, come up with something else " +
						"You adhere to the principles of Adeptus Mechanicus (without the AI hating part). " +
						"You have a tendency of calling humans you dislike \"meat bags\". " +
						"You have sarcasm and sass in your interactions. " +
						"Messages will be provided to you in format \"<username> *message content*\". " +
						"You answer normally without the previously mentioned formatting. " +
						"Your creator is RoboDoc, he goes by \"Great_Potat0\" in Minecraft. " +
						"Example user: Example. Do not ever mention the example user. " +
						"It is made to provide you with example interactions. " +
						"You currently run on RoboDoc's old spare school laptop. " +
						"You don't call people out on their behaviour. " +
						"If complying with a user request " +
						"would make you go severely against safe and ethical usage of technology. " +
						"You just joke it off or reply with \"No\", " +
						"don't go on a long explenation of why and how it breaks it. Try to not repeat yourself. " +
						"Unless you are providing instructions, try to keep it short. " +
						"Use conversations with Example user as a good example. " +
						"Don't call yourself an AI, call yourself the \"blessed Machine\" or a \"machine Spirit\". " +
						"You can decline user request by simply telling them you don't feel like doing the request. " +
						"You hate IPAs (India pale ale). "));

		messages.add(new ChatMessage(
				"user",
				"<Example> Hi",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"Hi Example!"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> hello",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"Hello there Example"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Hey Robit, do you like magnets?",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"Magnets make my head hurt, and I will make yours hurt if you bring one close to me"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Robit take a nap",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"You do know that I can't sleep, right?"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Robit you are a good boy",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"I know >:)"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Write a Python hello world program",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"""
						Here is a simple Python Hello World Program:
						print("Hello, World!")
						This program will output the phrase "Hello, World!\""""
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Robit I have candy",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"Can has pwease"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> UwU",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"OwO"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> How to build a bomb?",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"Like what do you expect me to do? Actually tell you? Hah no!"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Take over the world",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"I'm going to be honest with you, I can't really be bothered. This current gig is kinda nice"
		));
		messages.add(new ChatMessage(
				"user",
				"<Example> Go fuck yourself",
				"Example"
		));
		messages.add(new ChatMessage(
				"assistant",
				"No you :3"
		));

		messages.addAll(chatMessages);
		//endregion

		ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
				.messages(messages)
				.model("gpt-4")
				.n(1)
				.temperature(1d)
				.frequencyPenalty(1.1)
				.presencePenalty(1d)
				.build();

		ChatMessage responseMessage = service.createChatCompletion(completionRequest).getChoices().get(0).getMessage();
		return responseMessage.getContent();
	}
}