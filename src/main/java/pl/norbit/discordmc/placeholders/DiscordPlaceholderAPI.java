package pl.norbit.discordmc.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pl.norbit.discordmc.config.PluginConfig;
import pl.norbit.discordmc.players.DiscordPlayer;
import pl.norbit.discordmc.players.DiscordPlayerService;

public class DiscordPlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "discordmc";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Norbit";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if(params.equalsIgnoreCase("user_is_sync")){
            DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getGamePLayerByPlayerUUID(player.getUniqueId());

            if(gamePLayerByPlayerUUID.isSync()){
                return PluginConfig.TRUE_INFO;
            } else {
                return PluginConfig.FALSE_INFO;
            }
        } else if (params.equalsIgnoreCase("user_dc_name")){
            DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getGamePLayerByPlayerUUID(player.getUniqueId());

            if(gamePLayerByPlayerUUID.isSync()){
                return gamePLayerByPlayerUUID.getDiscordName();
            }
            return "";
        }else if (params.equalsIgnoreCase("user_full_dc_name")){
            DiscordPlayer gamePLayerByPlayerUUID = DiscordPlayerService.getGamePLayerByPlayerUUID(player.getUniqueId());
            if(gamePLayerByPlayerUUID.isSync()){
                return gamePLayerByPlayerUUID.getDiscordFullName();
            }
            return "";
        }
        return null;
    }
}


