# Fetch Coding Exercise

## Overview

This is my repository for my simple Android app to demonstrate my capabilities as a software engineer. It uses Retrofit to fetch the dataset from the provided URL, decodes it from JSON into model objects, filters out unnamed items, groups them by `listId`, sorts them, and displays them using Jetpack Compose. I also added a search bar for finding specific items in the selected list. After the data is fetched, it is also checked for inconsistencies in the naming convention, which there should be none. Finally, histogram values are logged to be entered into my Google Sheet for analysis.

## Design Choices

The color scheme I chose is based on the color scheme for the Fetch app on Google Play, but very simplified. If we're just showing some data to the user, we don't need bright, bouncy reds and yellows to grab their attention, so I went with just a relaxed low-opacity purple for the top bar and black/white for everything else.

Anytime you display a list of things that could potentially be long enough where the user has to scroll, a search field should be included at the top. The more items in the list, the more important the search bar is. Even though it wasn't directly asked of me, I felt it necessary to implement.

I chose to present the items as cards. Even though it takes up lots of space, it establishes that each item is individually important, and it allows for larger items with more properties to be implemented in the future. Note that only `name` is displayed; I don't need to show `listId`, because the items are already grouped into lists via that property, and `id` is contained in `name` as per the naming convention.

Why do I only show one list at a time? Either I can present all lists at once, or I could let the user select which list to show. The former option is bad, because it makes the grouping of items into lists pointless. While I do love gestures, they don't really make sense for this, so we need buttons. I could have one button for each list, but that doesn't work when you have a million lists. We need one button that lets you select which list to show, which is just a dropdown menu. We also need to convey which list is being shown, which we can do with a title in the top left.

I wanted to make the dropdown icon a chevron and put it just to the right of the title in the top leading corner. However, the android convention is to put core actions in the top trailing corner. I think this is a good convention to respect, because most people hold their phone in their right hand and use primarily their right thumb to interact. The top leading corner of the screen is the most difficult to reach area of the screen, so there should be absolutely no buttons there. It's also the reason why the title/search field is there, because it needs to always be visible. I could also make the icon a floating button somewhere else on the screen, but that looks unnecessarily ugly.

## Data Analysis

Based on my [analysis](https://docs.google.com/spreadsheets/d/1vpO0ZCqsGgOspcYmTJj0ma9_82-f4yJ8CCGzJBWLzoM/edit?usp=sharing), I think there was some human element involved in the creation of the dataset. The extreme categories 0...99 and 900...999 have the most elements by far, which is expected due to extremity bias. The second to last category 800...899 has the lowest count, which is likely due to its proximity to the last category. The second category doesn't experience this same effect because of low number bias, and because it bears some significance as the first 3-digit category. Similarly, the first category 0...99 has the highest count, because it bears strong significance as the lowest category and the only category with 1 and 2-digit numbers.
