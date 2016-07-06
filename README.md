# :iphone: *Book Listing*  

**Book Listing app** list the published books on a given topic using the Google Books API. 

## :sunglasses: User Stories

Layout:

* [x] App contains a ListView which becomes populated with list items.
* [x] List items display at least author and title information.
* [x] When there are more list items than fit on the screen, the app allows scrolling through the list.
* [x] The code adheres to all of the following best practices:
  * Text sizes are defined in sp
  * Lengths are defined in dp
  * Padding and margin is used appropriately, such that the views are not crammed up against each other.
* [x] Information displayed on list items is not crowded.
* [x] The content is not cut off if the device is rotated to landscape mode.

Functionality:

* [x] The app fetches data via an HTTP request from the Google Books API, using a class such as HttpUriRequest or HttpURLConnection.
* [x] The result of the request is validated to account for a bad server response or lack of server response.
* [x] The network call occurs off the UI thread using an AsyncTask or similar threading object.
* [x] The JSON response is parsed correctly, and relevant information is stored in the app.
* [x] The ListView is properly populated with the information parsed from the JSON response.
* [x] When there is no data to display, the app shows a default TextView that informs the user how to populate the list.

## :video_camera: Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://github.com/IsabelPalomar/android-book-listing/blob/master/BookListing.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIFs created with [LiceCap](http://www.cockos.com/licecap/).

