package growthcraft.lib.utils;

import net.minecraft.client.color.item.ItemColor;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility container for color-related helpers used across Growthcraft.
 * <p>
 * Currently contains the GrowthcraftColor value object which wraps a packed integer color and
 * provides multiple convenient representations for use with rendering and configuration code.
 */
public class ColorUtils {
    /**
     * Immutable color helper that exposes a color in several formats commonly used in the mod:
     * <ul>
     *   <li>Packed integer (RGB, 0xRRGGBB; alpha, if present in the packed int, is preserved only in the
     *   stored integer value).</li>
     *   <li>Normalized component map (red/green/blue/alpha in the range [0.0, 1.0]).</li>
     *   <li>Vector3f of RGB components (note: values are 0–255, not normalized).</li>
     *   <li>Minecraft ItemColor provider (for item tinting on layer 0).</li>
     * </ul>
     */
    public static class GrowthcraftColor {

        private final Color color;
        private final int colorIntValue;

        /**
         * Creates a new GrowthcraftColor from a packed integer.
         * <p>
         * Note: java.awt.Color(int) treats the value as 0xRRGGBB (opaque). If the provided integer
         * includes alpha (0xAARRGGBB), the alpha bits are not reflected in {@link #getColor()} but the
         * original integer value is preserved and returned by {@link #toIntValue()}.
         *
         * @param colorIntValue packed RGB or ARGB integer (e.g., 0xFFAA33 or 0xFFAABBCC)
         */
        public GrowthcraftColor(int colorIntValue) {
            color = new Color(colorIntValue);
            this.colorIntValue = colorIntValue;
        }

        /**
         * Returns the original packed integer color value used to construct this instance.
         *
         * @return packed RGB/ARGB integer
         */
        public int toIntValue() {
            return colorIntValue;
        }

        /**
         * Provides normalized RGBA components as a map in the range [0.0, 1.0].
         * Keys: "red", "green", "blue", "alpha".
         * <p>
         * Note: When constructed with an RGB-only value, the alpha component will be 1.0.
         *
         * @return map of component name to normalized float value
         */
        public Map<String, Float> toFloatValues() {
            Map<String, Float> floatMap = new HashMap<String, Float>();
            floatMap.put("red", this.color.getRed() / 255.0F);
            floatMap.put("green", this.color.getGreen() / 255.0F);
            floatMap.put("blue", this.color.getBlue() / 255.0F);
            floatMap.put("alpha", this.color.getAlpha() / 255.0F);
            return floatMap;
        }

        /**
         * Returns the RGB components as a Vector3f with each channel in the 0–255 range.
         *
         * @return Vector3f(R, G, B) with integer component values represented as floats
         */
        public Vector3f toVectorColor() {
            return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
        }

        /**
         * Returns the underlying java.awt.Color instance used for component access.
         *
         * @return Color instance (opaque if constructed from 0xRRGGBB)
         */
        public Color getColor() {
            return color;
        }

        /**
         * Converts this color to a Minecraft ItemColor implementation suitable for item tinting.
         * <p>
         * The returned ItemColor applies the packed integer color to layer 0, and returns opaque white
         * (0xFFFFFFFF) for all other layers.
         *
         * @return ItemColor that can be registered with the item color handler
         */
        public ItemColor toItemColor() {
            return (itemStack, layer) -> layer == 0 ? colorIntValue : 0xFFFFFFFF;
        }

    }
}
