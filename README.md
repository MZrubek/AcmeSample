# Sample application: Acme Shipment Routing
by Mike Zrubek, June 17, 2022


## Setup and Environment

Build and deploy from Android Studio. (I deployed to an emulator as well as to a real device).

Minimum SDK 25; build/target SDK 31

My environment was:
- Android Studio ArcticFox 3.1
- java 11.0.10, embedded JDK


## Design Approach

After reading the requirements, my thoughts for designing the app were about the overall structure,
processes, and presentation. This included some initial thinking about what the user would see and
how that data would be privided, without getting into any UI details.
- MVVM architecture is my default design approach. A ViewModel would provide the data by acquiring 
  it through background (IO thread) processing in a Coroutine, and present the data through a 
  LiveData instance for Observers to consume, keeping the ViewModel independent of any UI components.
- Use a data Repository to simulate a replaceable component for providing the data. Although 
  this exercise uses a single file-based data source, a real project would likely use RESTful services
  for obtaining data. Also for testing, a mock repository could be used as well. Each of these would
  be a different implementation. The ViewModel would be able to make use of the appropriate Repository.
- Model objects for representation, collection, and processing of various data items. Although some
  of the "values" are simply strings, in a real application, there would be more attributes around
  the objects and classes. In addition, additional methods for internal parsing and analysis of
  data relevant to the object are contained within the data classes (see Driver and Shipment details).
- UI structure. I prefer to use Fragments for display of content, so they can potentially be re-used
  in different presentation needs. (For example, a tablet in landscape mode might use a split-pane with
  fragment on each side, but in portrait mode only one fragment would show; or a fragment might be used
  in different places in the app). A single "Main" Activity would provide the UI for the fragment.
- Application structure for different packages. I tend to separate different components of an app into
  different file packages/folders. Teams often have a preference on how files are structured, but
  when there is no prior preference, consideration of an easy-to-navigate project structure is 
  important so that files can quickly be found, especially as an app gets larger. In addition to this
  structure I used, I also will use more of a feature-oriented approach where everything specific
  to that feature is within the same package, including activities, fragments, viewmodels, etc.
  The use of a "features" package would relate more to evolution of an app, includign initial and
  enhancement sprint-planned features.
- Modules, custom libraries, and third-party libraries. A larger app would make use of common code
  where application that may be created for sharing between apps. For example, a library for various
  queries of shopping items or products, or common UI widgets and custom classes. None of these were
  used here. Third-party libraries would also be used when reliable (and stable) implementations are 
  available. For example, I used Gson for parsing Json and subsequent object deserialization.
- Build and deployment factors. No specific consideration was given for different build flavors, CI/CD,
  nor deployment. Jenkins or Bitrise would be my assumption for automatic build processing, once
  code was checked into github, for example. And, no Version Control System was used, other than
  adding the entire project to my Git repository upon completion.
- Test cases. I created one unit test in androidTest in ExampleInstrumentedTest, to test the reading
  and creation of the AcmeData class from the json file.
  

## Data Model

The following model objects represent the source data and processed results:
- Driver - holds truck driver data, including name and name characteristics.
- Shipment - holds shipment details, including delivery address.
- AcmeData - data source json target for deserialization of data, holding raw driver and shipment strings.
- Delivery - associated Driver and Shipment, along with related attributes, such as suitability score.
- Deliveries - holds a collection of Delivery instances; used more of a convenience wrapper around a list.
- Suitability - working object for associating a Driver and Shipment and calculating a combined suitability score. 


## Processing Notes

The flow and analysis of data within the ViewModel (DeliveriesViewModel) is such that first, the
raw data is obtained from the repository, in a collection of AcmeData instances. Then, the data
is applied to ShipmentAssigner to create and analyze suitability scores (SS), and create Delivery
instances as a result of chosen Driver-Shipment combinations.


## UI Features

The UI is a very simple 1-fragment view of data relevant to a Delivery (Driver, Shipment, suitability score).
A recycler view used to hold the list of Delivery items, and then a sample but simple custom layout
is used to present them. A simple progress-bar type of image is used to show a range of suitability
scores, along with the actual value of the score; I opted to use this to provide the calculated
score as well as a quick-visual as to how a score relate to other scores in the list.

The list is sorted by suitability score descending order. for this exercise. A better solution
would be to add sorting options to sort by other characteristics, like driver name (first or last),
shipment destination, etc. And a find option could be added to find specific matches and reduce list size.

The list is a single collection of Deliveries, all provided at the same type (by the ViewModel), and
then all set into the recycler Adapter at one time. No provision is made for more piecemeal or chunks
of data that the ViewModel could provide such as from a large list of data, or as needed if user
scrolls down a long list.

An interface callback is included (stubbed-out) in the fragment to handle click events on a list
item. Typical use would be to show a more detailed view of the data, follow-up actions, etc.

As a little simulation upon startup of where data could take time to load, I am showing a progress
spinner when the app starts. This is purposely caused by a delay coded in the ViewModel, so that
such a loading indicator could be shown, whether it be a splash screen, or a real screen when a 
long-loading process takes place in the background.

Errors are not displayed, but are very roughly handled by passing errorMessage strings across
some of the objects, if an error would occur. Typically, I'll use a more structured Error class
approach, where an error situation has more details; of course just passing the Exception is a good 
approach as well. But in terms of UI, I have a stub in place in the fragment Observer to optionally
handle an error condition; this is not implemented.


## Assumptions

- driver and shipment counts are the same in the sample data. This is not likely a valid scenario,
and no coding assumptions where made around this equality. Calculated deliveries are made only for
the provided number of drivers or shipments, whichever is smaller.

- shipment address evaluation. For scoring, only part of the address is used; the leading numeric
portion and optional modifiers like "apt." or "suite" are ignored.

- suitability scoring process has numerous assumptions and some ambiguities, 
as noted in the Suitability class. 

