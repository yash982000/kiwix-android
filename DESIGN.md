# Design

This file intends to provide information on how to work with design in the Kiwix android applicaiton. 

## Colors

<img src="https://user-images.githubusercontent.com/22193232/83739760-4ef07080-a656-11ea-8f87-344f48e76324.png" alt="drawing" width="300"/>

The Kiwix colors are black and white with different colored accents. For Kiwix Android, black is accented by different shades of blue:

- ![#000000](https://via.placeholder.com/15/000000/000000?text=+) `#000000`/`black`
- ![#1565c0](https://via.placeholder.com/15/1565c0/000000?text=+) `#1565c0`/`denim_blue800`
- ![#42a5f5](https://via.placeholder.com/15/42a5f5/000000?text=+) `#42a5f5`/`denim_blue400`
- ![#2196F3](https://via.placeholder.com/15/2196F3/000000?text=+) `#2196F3`/`dodger_blue`

> Color indicates which elements are interactive, how they relate to other elements, and their level of prominence. Important elements should stand out the most [1].

For a guide on how to apply colors to UI, see ["Applying color to UI - Material Design"](https://material.io/design/color/applying-color-to-ui.html#backdrop).

## Themes
Kiwix uses themes to apply styles to views. This means that instead of adding a specific color or style to each view in the application, a theme should be set.
Themes are defined in `res/values/themes.xml` or `res/values-night/themes.xml` depending on night/day.
The following steps should be followed to set a color or style of a view.
1. Define the color or style:
    * Define the color in `core/colors.xml`. Use good naming (`denim_blue`, `mine_shaft_gray` etc..). Our recommended tool for color naming is [Chirag Mehta - Name that color](http://chir.ag/projects/name-that-color/#6195ED).
    * Define the style in `core/styles.xml`. For our default styles, applied with theme attributes, the naming convention is `Widget.KiwixTheme.*` and these should ideally inherit from their `Widget.MaterialComponent.*`, should no such parent exist, it is generally advisable to google "how to style [Component]". When there are two or more of a view that differ stylistically from our default styling, but are identical to each other visually, it is appropriate to extract a style to keep the XML DRY. A non-default style should use a descriptive name. e.x. `list_item_title`. 
2. Add the color or style to a specific theme attribute in `themes.xml`.
3. Make sure that the color or style works in both day and night mode. If it does not, add the dark mode compatible attribute to `values-night/themes.xml`.

For a video on how to work with themes, styles and colors, see [Developing Themes with Style (Android Dev Summit '19)](https://www.youtube.com/watch?v=Owkf8DhAOSo).

## Night Mode / Dark Theme
Night mode is a different theme that has many benefits such as saving battery and improving visibility in low light conditions [2]. When doing any design, make sure the new design works in dark mode. Night mode can be activated from the systems settings or from the kiwix app. 

Whenever a resource exists in both `res/*-night` and `res/*` it will be used as such. For example: `kiwix_icon_with_title.png` exists in both `res/drawable` and `res/drawable-night` which means that the image in `res/drawable-night` will be used automagically in night mode. For further reading on development with dark mode, see [Material Design - Dark Theme](https://developer.android.com/guide/topics/ui/look-and-feel/darktheme). 

## Typography
Text should contrast well, use the correct weight and size to present content as clearly and efficiently as possible. 

Views using the same style often use different text style. Therefore text styles, and view styles are seperated and can be used simultaneously. To keep an android application consistent in style, thirteen text style attributes should be defined. These style attributes can be generated ([Material Design type scale generator](https://material.io/design/typography/the-type-system.html#type-scale)), or defined by editing `core/../values/type.xml`. 

All text should have a text style to simplify appearance changes. To set a text style, simply set the `textAppearance` property using one of the thirteen text style attributes from `core/../values/type.xml`. Text can also be grouped if they are related and uses the same text style. This grouping can be done by creating a style that inherits, or sets a specific text appearance. E.x. `<style name="no_content_text" parent="TextAppearance.KiwixTheme.Headline5" />`.

Additional reading can be found at [Material Design - Type System](https://material.io/design/typography/the-type-system.html#type-scale). 

## Resources
Following is a list of resources that can and should be used to support a design decision.

1. [Material Design](https://material.io/) - Googles design guide that should be followed to keep Android applications consistent.
2. [Design for Android](https://developer.android.com/design) - Another design guide by Google, this one is specifically for Android while Material Design is more general.
3. [Figma](https://www.figma.com) - A tool that can be used to mock design ideas.

## Accessibility
Accessibility is an important part of Android applications. Especially applications that are as widely used as Kiwix. As such, accessibility should be part of design decisions. This can include choices such as:

- Having text with a large font and good contrast ratio.
- Large simple controls.
- Consistent controls and navigation. (consistent to Kiwix, and other Android applications.)

Kiwix does not yet contain content descriptions for its UI elements. This can prohibit users with for example poor eyesight to use Kiwix successfully. Therefore, newly introduced interactive elements should contain content descriptions (boy scout rule applies here too!). 

See ["Make apps more accessible"](https://developer.android.com/guide/topics/ui/accessibility/apps) for a more detailed guide on accessibility. [Firefox Fenix](https://github.com/mozilla-mobile/shared-docs/blob/master/android/accessibility_guide.md) also has a great concise description of accessibility. A third accessibility resource is googles [list of key steps](https://android-developers.googleblog.com/2012/04/accessibility-are-you-serving-all-your.html) for making sure an Android application is accessible.
<br/>
<br/>
<br/>
<br/>

[1] https://material.io/design/color/the-color-system.html 2020-06-04

[2] https://developer.android.com/guide/topics/ui/look-and-feel/darktheme 2020-07-02


