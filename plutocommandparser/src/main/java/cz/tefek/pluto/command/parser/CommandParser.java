package cz.tefek.pluto.command.parser;

import java.util.PrimitiveIterator.OfInt;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cz.tefek.pluto.command.CommandBase;
import cz.tefek.pluto.command.context.CommandContextBuilder;
import cz.tefek.pluto.command.context.CommandContextBuilder.CommandContext;
import cz.tefek.pluto.command.context.CommandContextBuilder.EnumCommandParseFailure;
import cz.tefek.pluto.command.registry.CommandRegistry;

public class CommandParser
{
    private final String text;
    private Set<OfInt> prefixes;
    private EnumParserState state;

    private StringBuilder prefixBuilder;
    private StringBuilder commandNameBuilder;

    private CommandBase command;

    private StringBuilder parameterBuilder;

    private CommandContextBuilder ctx;

    private static final int CP_QUOTE = '"';

    public CommandParser(String text)
    {
        this.text = text;
        this.state = EnumParserState.BEGIN;
    }

    private boolean readCodepoint(int cp)
    {
        switch (this.state)
        {
            case READING_PREFIX:
                this.prefixBuilder.appendCodePoint(cp);

                this.prefixes.removeIf(ii -> ii.nextInt() != cp);

                if (this.prefixes.isEmpty())
                {
                    this.state = EnumParserState.END_NO_PREFIX;
                    return false;
                }

                if (this.hasEmptyPrefix())
                {
                    this.ctx.prefix(this.prefixBuilder.toString());
                    this.state = EnumParserState.READING_COMMAND;
                }

                break;

            case READING_COMMAND:
                this.commandNameBuilder.appendCodePoint(cp);

                if (Character.isWhitespace(cp))
                {
                    if (!this.resolveCommand())
                    {
                        return false;
                    }

                    this.state = EnumParserState.READ_WHITESPACE;
                }

                break;

            case READ_WHITESPACE:
                if (Character.isWhitespace(cp))
                {
                    break;
                }

                if (cp == CP_QUOTE)
                {
                    this.state = EnumParserState.READING_PARAMETER_QUOTED;
                }
                else
                {
                    this.parameterBuilder.appendCodePoint(cp);
                    this.state = EnumParserState.READING_PARAMETER;
                }

                break;

            case READING_PARAMETER_QUOTED:
                if (cp == CP_QUOTE)
                {
                    this.state = EnumParserState.READING_PARAMETER_CANDIDATE_UNQUOTE;
                }
                else
                {
                    this.parameterBuilder.appendCodePoint(cp);
                }

                break;

            case READING_PARAMETER:
                if (Character.isWhitespace(cp))
                {
                    this.emitParameter();
                    this.state = EnumParserState.READ_WHITESPACE;
                }
                else
                {
                    this.parameterBuilder.appendCodePoint(cp);
                }

                break;

            case READING_PARAMETER_CANDIDATE_UNQUOTE:
                if (Character.isWhitespace(cp))
                {
                    this.emitParameter();
                    this.state = EnumParserState.READ_WHITESPACE;
                }
                else
                {
                    this.parameterBuilder.appendCodePoint(cp);
                    this.state = EnumParserState.READING_PARAMETER_QUOTED;
                }

                break;

            case END:
                this.state = EnumParserState.UNEXPECTED_STATE_FALLBACK;
                return false;

            default:
                this.state = EnumParserState.UNEXPECTED_STATE_FALLBACK;
                return false;
        }

        return true;
    }

    private boolean resolveCommand()
    {
        var alias = this.commandNameBuilder.toString();

        this.ctx.alias(alias);

        this.command = CommandRegistry.getByAlias(alias);

        if (this.command == null)
        {
            this.state = EnumParserState.END_NO_COMMAND;
            return false;
        }

        return true;
    }

    private void emitParameter()
    {

    }

    private boolean hasEmptyPrefix()
    {
        return this.prefixes.stream().anyMatch(Predicate.not(OfInt::hasNext));
    }

    /**
     * Parse using this parser and supplied prefixes. This function also
     * resolves the command context and the parameters. Yeah it does a lot of
     * stuff.
     * 
     */
    public CommandContext parse(Set<String> prefixes)
    {
        if (this.state != EnumParserState.BEGIN)
        {
            throw new IllegalStateException("Cannot run a parser that is not in the BEGIN state.");
        }

        this.prefixBuilder = new StringBuilder();
        this.ctx = new CommandContextBuilder();

        this.prefixes = prefixes.stream().map(String::codePoints).map(IntStream::iterator).collect(Collectors.toSet());

        if (prefixes.isEmpty() || this.hasEmptyPrefix())
        {
            this.state = EnumParserState.READING_COMMAND;
        }
        else
        {
            this.state = EnumParserState.READING_PREFIX;
        }

        var cps = this.text.codePoints();

        for (var cpIt = cps.iterator(); cpIt.hasNext(); )
            if (!this.readCodepoint(cpIt.next()))
                break;

        // Update the state for EOF        
        switch (this.state)
        {
            case READING_PARAMETER_QUOTED:
            case READ_WHITESPACE:
            case READING_PARAMETER:
            case READING_PARAMETER_CANDIDATE_UNQUOTE:
                this.state = EnumParserState.END;
                this.emitParameter();

                break;

            case READING_COMMAND:
                if (this.resolveCommand())
                {
                    this.state = EnumParserState.END;
                }

                break;

            default:
                break;
        }

        // Check the end state
        switch (this.state)
        {
            case READING_PREFIX:
            case END_NO_PREFIX:
                return this.ctx.unresolved(EnumCommandParseFailure.UNRESOLVED_PREFIX);

            case END_NO_COMMAND:
                return this.ctx.unresolved(EnumCommandParseFailure.UNRESOLVED_COMMAND_NAME);

            case END:
                break;

            default:
                return this.ctx.unresolved(EnumCommandParseFailure.UNRESOLVED_UNEXPECTED_STATE);
        }

        // At this point we are 100% sure the command was resolved and can validate the parameters  

        /*
         * 
         * TODO: Validate parameters here
         * 
         */

        return this.ctx.resolved();
    }
}
