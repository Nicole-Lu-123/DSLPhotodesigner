Add entries to this file summarising each project milestone. Don't forget that these need to have been discussed with your TA before the milestone deadline; also, remember to commit and push them by then!

# Milestone 1

Old DSL idea:
Flappy bird game
We intend to make it easier and simpler for users to create and define the gameplay and interface, we intend to add more functional options, such as more obstacles and different functional items, and users can also change the game border according to their personal needs. 

In this case, users can using our predefined DSL to achieve some gaming set up tasks listed:

background setting(size and color),bird setting(size,color,number of birds and fly direction and speed), obstacles setting( number of obstacles, position of them(distance between a pair) and movment, Props position/features setting(number of propos (become bigger, smaller, invincible...) and the position of them). 


TA feedback:
Although games have been done in the past, it is generally ok to overlap with a previous idea as long as you have something novel in the detail. The main thing we want is for the language to have something powerful in terms of features. A good DSL project should allow user to get creative and steer away from looking like a form with a subset of input to chose from. I would suggest doing a bit more brain storming together as a group on the possible features to add. TA suggested that we can use HTML and CSS to display the interface effect, For example, some special effects will appear when the clicks or swipes the mouse.

New DSL idea:
The new idea is to create a timeline of the given lists of books and articles using html and css.

For example, the user gives a list of books and articles with the title, publishing year, author, published place, and some other details. The webpage will sort the books based on the publishing date in increasing order, and generate a timeline associated with a book. Each time slot points to a label which contains all the information of that book. 

Grammer example:
create timeline
create book1: title: cpsc410, author: team 16, year: 2020
create book2: title: cpsc310, author: team 16, year: 2019
add book1 and book2 to timeline
sort based on year

Follow-up tasks and features:
Not only the publishing date, the users can sort any fields that they want to sort.
For example, the author can be alphabetic sorted, but showing in a another way rather than timeline.
Sorting the location of the publisher alphabetically and labelling them in a map is also a good way.

(We will make an appointment with TA to discuss our new DSL idea)



# Milestone 2

- Planned division of main responsibilities between team members

  - Sunny: contact with TA, write grammar, write frontend photoeditor viewer (xml)
  - Difei: work on photoeditor, textstyle builder, saving photo after editing
  - Nicole: write user examples and frontend
  - Tian: Write the grammar for this project and implement some Backend such as photo mosaic 


- Roadmap for what should be done then

  - Improve the functions and features of Photo Editor
  - Complete the grammar
  - General Structure (java, html + css + javascript)
  - Write the user examples
  - Implementation
  - Debug
 

- Summary of progress so far
  - We have changed 3 DSL ideas so far, so the work we've done it may be a bit slow compare to the other groups. But we believe we are in the right track. 
  - We have decided the main functions of our photo editor and write the grammar in a simple way, but we will improve it later.


- Here is our new DSL idea:

We planned to do photo editor. The users are allowed to do any transformation of one photo or multiple photos and the final result will present in the website to show the before and after effect.

- Photo editor:
  - With only 1 Photo, the photo editor has the following effect:
    - Convert color 
    - Blurry (users can give a filter function to customize how they blur the photo)
    - Transformation (crop, rotate, flip)
    - Present in a particular pattern (user can write function)
    - Change transparency (0~100%)
    - Add and edit text 
 
  - For more than one photos, the photo editor has the following effect:
    - Photo Collage = putting multiple photos together(set their positions, rotations)
    - Photo Mosaic = repeat given photos thousands of time and combine them into one logo (simple shape)
    
    
    
# Milestone 3
- User Study 
  - Feedback from users:
    - having difficulty in understanding how to write "Blur", gramma says using STRING but example uses a list of lists and don't know how to use this method to blur.  (TODO: maybe we can write blur in an easier way)
    - position is a point (position should be a point)
    - any bound for numbers like size, position, ...? (yes, we need bounds, but it is validation part which will be completed by the parser.)
    - no idea about the position and size. For example, when the user want to work on left conner of the picture, the size and position can't be determind. (TODO: we can divide the picture and show the divide lines on the picture before user run the program)
    - no angle in rotate. (change "left,right" to "clockwise, counter-clockwise", but considering the difficulty of adding the factor of angle, we just keep the feature of rotate only 90 degree of  given area)
    - for LOC, not really clear what is it. (Use "LOCATION" instead)
    - a little strange to have "," in the name (change that NAME can be any string)
    - suggestion: the range can be a list of ranges, so that editor can change several areas together.
    
    
- Fixed Grammar
```
PROGRAM :: = "TITLE:" STRING INSTRUCTIONS
INSTRUCTIONS ::= CREATE RUN 
CREATE ::= "CREATE" NAME" {" LOCATION, VAR+ "}"
RUN :: = "RUN"
LOCATION :: = "insert from:" STRING
VAR :: = "var" NAME "{" RANGE FUNCTIONS? "}"
RANGE::= "range:" ("all" | RANGEA | RANGEB)
RANGEA ::= "[" NUM "," NUM "]" "," "[" NUM "," NUM "]"
RANGEB::= "{" "shape:" SHAPE  "size:" NUM  "position:" POSITION "}"
SHAPE::= "circle"|"star"|"triangle"|"bomb"|"rectangle"|"square"
POSITION::= (NUM,NUM) POSITION 
FUNCTIONS ::=  ADJUST | TRANSFORMATION| BLUR 
ADJUST :: = TRANSPARENCY | SATURATION | BRIGHTNESS
TRANSPARENCY:: = "transparency:" NUM
SATURATION ::= "saturation:" SIGN NUM
BRIGHTNESS ::= "brightness:" SIGN NUM
SIGN ::= "+" | "-"
TRANSFORMATION ::=  ROTATE | FLIP 
ROTATE ::= "rotate: counterclockwise" | "rotate: clockwise" 
FLIP ::= "flip"
BLUR ::= "blur:" STRING
MOSAIC ::= "Mosaic" 
NAME ::= STRING
STRING ::= [^:]+
NUM :: = [0-9]+
```

- Follow-up tasks
  - Make it easier and more efficient for users to select range
  - Blur function more user-friendly (idea: user can choose to blur in three different levels or write filter function themselves)
  - Add user-defined transformation functions
  
  # Milestone 4
 
- status of implementation
  - Our group is a little behind now, but we plan to start writing project this weekend
  - We have spent large portion of the time on planning out small detail of features our program will have; given we are having a long weekend, we are planning to catch up and offer deliverable in a timely manner. 

- Plans for final user study:
  - We have reached out to different group of potential users, some are arts students with little or no knowledge of programming and others are knowledgable full time engineers with no relevant art background. We are planning to conduct two rounds of user studies by the mid of Week 6. 
```
PROGRAM :: = CREATE RUN
CREATE ::= "CREATE" NAME " {" PATH FUNC+ VAR+ "}"
RUN :: = "RUN"
PATH :: = "path:" STRING
FUNC ::= "func" FUNCNAME "{" FUNCTIONS+ "}"
VAR :: = "var" NAME "{" RANGE EFFECT "}"
RANGE::= "range:" ("full" | RANGE')
RANGE' ::= "{" "size:" SIZE "position:" POSITION "}"
EFFECT ::= "effect" ":" FUNCNAME
SIZE ::= NUM "," NUM
POSITION::= NUM "," NUM
FUNCTIONS ::= INSTRUCTION*
INSTRUCTION::= TRANSPARENCY | SATURATION | BRIGHTNESS| ROTATE | FLIP |MOSAIC
TRANSPARENCY:: = "transparency" ":" NUM
SATURATION ::= "saturation" ":" SIGN NUM
BRIGHTNESS ::= "brightness" ":" SIGN NUM
SIGN ::= "+" | "-"
ROTATE ::= "rotate:" DIRECTION "," NUM
DIRECTION ::= "counterclockwise" | "clockwise"
FLIP ::= "flip"
MOSAIC ::= "mosaic"
STRING ::= [^:{}]+
NAME ::= [a-zA-Z0-9]+
FUNCNAME ::= [a-zA-Z0-9]+
NUM :: = [0-9]+
```

- Planned timeline for the remaining days:
  - Oct 10th: implement the tokenization and parsing
  - Oct 11th - Oct 14th: implement Evaluation and some detailed function for this project
  - Oct 15th - Oct 18th: implement frontend user interface
  - Oct 18th - Oct 19th: filming and editing video for submission
