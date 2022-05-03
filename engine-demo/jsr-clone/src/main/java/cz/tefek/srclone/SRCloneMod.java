package cz.tefek.srclone;

import org.plutoengine.audio.AudioLoader;
import org.plutoengine.audio.RandomAccessClip;
import org.plutoengine.audio.SeekableTrack;
import org.plutoengine.graphics.PlutoGUIMod;
import org.plutoengine.graphics.vao.QuadPresets;
import org.plutoengine.graphics.vao.VertexArray;
import org.plutoengine.graphics.gui.font.bitmap.BitmapFont;
import org.plutoengine.graphics.gui.font.stbttf.STBTTFont;
import org.plutoengine.graphics.texture.texture2d.RectangleTexture;
import org.plutoengine.libra.text.font.LiFontFamily;
import org.plutoengine.libra.text.shaping.TextStyleOptions;
import org.plutoengine.mod.IModEntryPoint;
import org.plutoengine.mod.Mod;
import org.plutoengine.mod.ModEntry;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import cz.tefek.srclone.graphics.DirectionalSprite;
import cz.tefek.srclone.graphics.StarField;

@ModEntry(modID = "tefek.srclone", version = "0.1", dependencies = PlutoGUIMod.class)
public class SRCloneMod implements IModEntryPoint
{
    public static LiFontFamily<STBTTFont> font;

    public static LiFontFamily<BitmapFont> srCloneFont;

    public static SeekableTrack playingMusic;

    public static RandomAccessClip[] explosionSound;
    public static RandomAccessClip impactSound;
    public static Map<String, RandomAccessClip> shootSounds;

    public static VertexArray centeredQuad;

    public static RectangleTexture[] stars;
    public static StarField starField;

    public static RectangleTexture projectilesBase;

    public static DirectionalSprite player;
    public static RectangleTexture rocketNozzle;

    public static DirectionalSprite enemyScout;
    public static DirectionalSprite enemySmallBomber;

    public static DirectionalSprite pickupBox;

    public static DirectionalSprite parExplosion;

    public static DirectionalSprite parImpact;

    @Override
    public void onLoad(Mod mod)
    {
        font = new LiFontFamily<>();
        font.add(TextStyleOptions.STYLE_REGULAR, STBTTFont.load(mod.getResource("plutofonts$plutostardust#ttf")));

        srCloneFont = new LiFontFamily<>();
        srCloneFont.add(TextStyleOptions.STYLE_REGULAR, BitmapFont.load(mod.getResource("plutofonts$srclone.info#yaml")));

        playingMusic = AudioLoader.loadMemoryDecoded(mod.getResource("sound.game_st#ogg"));

        explosionSound = IntStream.range(0, 5)
                                  .mapToObj("sound.explosion%d#ogg"::formatted)
                                  .map(mod::getResource)
                                  .map(AudioLoader::loadMemoryPCM)
                                  .toArray(RandomAccessClip[]::new);
        impactSound = AudioLoader.loadMemoryPCM(mod.getResource("sound.hit#ogg"));

        shootSounds = Stream.of("laser", "heatStar", "polySwarm", "electronFlare", "plasma", "tachyon")
                            .collect(Collectors.toMap(Function.identity(), name -> {
                                var path = "sound.shoot.%s#ogg".formatted(name);
                                var resource = mod.getResource(path);
                                assert resource != null;
                                var audio = AudioLoader.loadMemoryPCM(resource);
                                assert audio != null;
                                return audio;
                            }));

        centeredQuad = QuadPresets.halvedSize();

        stars = Stream.of("blue", "red", "redDwarf", "whiteDwarf", "blue").map(starName -> {
            var tex = new RectangleTexture();
            tex.load(mod.getResource("textures.stars.%s#png".formatted(starName)));
            return tex;
        }).toList().toArray(RectangleTexture[]::new);

        starField = new StarField(0, 0, 8192, 8192, stars);

        projectilesBase = new RectangleTexture();
        projectilesBase.load(mod.getResource("textures.particles.projectilesBase#png"));

        player = DirectionalSprite.create(mod.getResource("textures.entities.ship#png"), 32, 64, 64, 32);
        rocketNozzle = new RectangleTexture();
        rocketNozzle.load(mod.getResource("textures.particles.rocketNozzle#png"));

        enemyScout = DirectionalSprite.create(mod.getResource("textures.entities.e_scout#png"), 16, 128, 128, 16);
        enemySmallBomber = DirectionalSprite.create(mod.getResource("textures.entities.e_small_bomber#png"), 16, 128, 128, 16);

        parExplosion = DirectionalSprite.create(mod.getResource("textures.particles.explosion1#png"), 16, 128, 128, 16);
        parImpact = DirectionalSprite.create(mod.getResource("textures.particles.impact1#png"), 10, 64, 64, 10);

        pickupBox = DirectionalSprite.create(mod.getResource("textures.entities.box#png"), 16, 64, 64, 16);
    }

    @Override
    public void onUnload()
    {
        pickupBox.close();

        parImpact.close();
        parExplosion.close();

        enemySmallBomber.close();
        enemyScout.close();

        rocketNozzle.close();
        player.close();

        projectilesBase.close();

        for (var star : stars)
            star.close();

        centeredQuad.close();

        shootSounds.values()
                   .forEach(RandomAccessClip::close);

        impactSound.close();

        for (var track : explosionSound)
            track.close();

        playingMusic.close();

        srCloneFont.close();

        font.close();
    }
}
