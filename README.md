**Location Tracker Application**

**Installation**
- Open this project using Android Studio
- Build this project by connecting your phone to your computer then press the 'run' green arrow button.

**Implementation trade-offs**
Due to time constrains
- Simple architecture without any go to libraries
- Unit test doesn't work correctly, as it is testing against Android api. No mocks were in used
- Simple UI for speed

**Any architectural considerations and reasoning**
- No specific architecture like MVVM or MVI, if it was a commercial project might leverage on VM and Livedata for business logic and UI change
- Main functions are in a single activity with a utility class, as this is a poc

**Your areas of focus, and anything you would like to draw our attention to**
- Demonstrates all functions are working and what a unit test would look like

**Any copied code, references and 3rd party libraries**
- All gps related code where found in Google's documentation
- Had to add Google Play dependency in order for gps functionality to work

**How long you worked on the app for**
- Because I was working on this after working hours, so I had people come and talk to me. Give or take, around 4 hours

**Anything else you want us to know**
- Please be mindful this is a poc, so I chose to use Android api to do distance calculation therefore unit test doesn't fully work. However, in a commercial project a mock would be used.
