package gd.rf.acro.ace;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import gd.rf.acro.ace.spells.Spell;
import gd.rf.acro.ace.spells.Spells;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SpellArgumentType implements ArgumentType<String> {


    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public Collection<String> getExamples() {
        return Spells.REGISTRY.stream().map(Spell::name).collect(Collectors.toList());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        return CommandSource.suggestMatching(Spells.REGISTRY.stream().map(Spell::name),builder);
    }


    public static class Serializer implements ArgumentSerializer<SpellArgumentType> {
        public Serializer() {
        }


        @Override
        public void toPacket(SpellArgumentType type, PacketByteBuf buf) {

        }

        public SpellArgumentType fromPacket(PacketByteBuf packetByteBuf) {

            return new SpellArgumentType();
        }

        @Override
        public void toJson(SpellArgumentType type, JsonObject json) {

        }


    }

}
