package best.sti2d.therese.utils;

import best.sti2d.therese.Therese;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmbedCrafter {

    private String title;
    private int color = 15528177;
    private String description;
    private List<MessageEmbed.Field> fields = new ArrayList<>();
    private String thumbnailUrl;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public EmbedCrafter setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getColor() {
        return color;
    }

    public EmbedCrafter setColor(int color) {
        this.color = color;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EmbedCrafter setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<MessageEmbed.Field> getFields() {
        return fields;
    }

    public EmbedCrafter addField(MessageEmbed.Field field) {
        this.fields.add(field);
        return this;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public EmbedCrafter setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public EmbedCrafter setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public EmbedBuilder build(){
        Therese therese = Therese.getInstance();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
            .setTitle(title)
            .setColor(color)
            .setDescription(description)
            .setThumbnail(thumbnailUrl)
            .setImage(imageUrl)
            .setFooter(therese.getConfigurationManager().getStringValue("embedFooter"), therese.getConfigurationManager().getStringValue("embedIconUrl"))
            .setTimestamp(LocalDateTime.now());
        fields.forEach(embedBuilder::addField);
        /*if(thumbnailUrl != null) embedBuilder.setThumbnail(thumbnailUrl);
        if(imageUrl != null) embedBuilder.setImage(imageUrl);*/
        return embedBuilder;
    }

}
