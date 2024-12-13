# Terminal Screen 

## Overview

The Terminal Screen Project simulates rendering operations on a terminal-based screen using a custom binary protocol. This project involves a server-client setup where the server listens for commands related to screen rendering, and the client sends commands to the server to manipulate the terminal screen. 

The communication between the client and the server is based on a byte-stream protocol, with each command being formatted as a sequence of bytes. The system supports various operations, such as setting up the screen, drawing characters, lines, rendering text, moving the cursor, and more.

## Features
- **Screen Setup (0x1):** Configure screen dimensions and color modes.
- **Draw Character (0x2):** Place characters at specific coordinates.
- **Draw Line (0x3):** Draw lines between two coordinates using a specified character.
- **Render Text (0x4):** Render a string at a specified position.
- **Cursor Movement (0x5):** Move the cursor to a specific location.
- **Draw at Cursor (0x6):** Draw a character at the current cursor position.
- **Clear Screen (0x7):** Clear the screen.
- **End of File (0xFF):** Marks the end of the binary stream.

### Communication Format

The communication between the client and server is based on a binary format with the following structure:
+--------------+-------------+-------------+-------------+----···---+----------------+ | Command Byte | Length Byte | Data Byte 0 | Data Byte 1 | ··· | Data Byte n-1 | +--------------+-------------+-------------+-------------+----···---+----------------+


The data format for each command is as follows:

### Command Definitions

1. **0x1 - Screen Setup**
    - **Data Format**:
        - Byte 0: Screen Width (in characters)
        - Byte 1: Screen Height (in characters)
        - Byte 2: Color Mode (0x00 for monochrome, 0x01 for 16 colors, 0x02 for 256 colors)

2. **0x2 - Draw Character**
    - **Data Format**:
        - Byte 0: x-coordinate
        - Byte 1: y-coordinate
        - Byte 2: Color index
        - Byte 3: Character to display (ASCII)

3. **0x3 - Draw Line**
    - **Data Format**:
        - Byte 0: x1 (starting x-coordinate)
        - Byte 1: y1 (starting y-coordinate)
        - Byte 2: x2 (ending x-coordinate)
        - Byte 3: y2 (ending y-coordinate)
        - Byte 4: Color index
        - Byte 5: Character to use (ASCII)

4. **0x4 - Render Text**
    - **Data Format**:
        - Byte 0: x-coordinate
        - Byte 1: y-coordinate
        - Byte 2: Color index
        - Byte 3-n: Text data (ASCII characters)

5. **0x5 - Cursor Movement**
    - **Data Format**:
        - Byte 0: x-coordinate
        - Byte 1: y-coordinate

6. **0x6 - Draw at Cursor**
    - **Data Format**:
        - Byte 0: Character to draw (ASCII)
        - Byte 1: Color index

7. **0x7 - Clear Screen**
    - **Data Format**: No additional data.

8. **0xFF - End of File**
    - **Data Format**: No additional data.

---

## How to Run the Application

Follow these steps to clone, build, and run the application.

### Prerequisites

- **Java 8 or above** installed on your system.
- A terminal or command prompt.

### Step 1: Clone the Repository

Start by cloning the repository to your local machine:

```bash
git clone <https://github.com/JamesWainaina/Terminal-Screen.git>
```

### Step 2: Change directory to src folder
```bash
cd src
```

### Step 3: Compile the code
```bash
javac Main.java
```

### Step 4: Run the server
```bash
java Main Server
```

### Step 5: Run the client
```bash
java Main client localhost 8000
```

### Step 5: Send the commands 
```bash
0x1:10,10,0
```



## What I love most about computing

What I love most about computing is the power it has to solve problems, create new possibilities, and transform the way we interact with the world. Whether it's using algorithms to analyze data, enabling creative expression through design and art, or providing a platform for people to connect and collaborate, computing offers endless opportunities to innovate. I also find the way it constantly evolves and pushes the boundaries of what's possible—like advances in AI, quantum computing, and virtual reality—fascinating and inspiring! 


## If you could meet any scientist or engineer who died before A.D. 2000
If I could meet any scientist or engineer who died before A.D. 2000, I would choose Nikola Tesla. Tesla was a brilliant inventor, electrical engineer, and physicist, known for his groundbreaking work in alternating current (AC) power systems, wireless communication, and numerous other innovations that laid the foundation for much of modern technology.

The reason I would choose Tesla is that he was not only a visionary who could foresee technological advancements far ahead of his time but also an enigmatic figure whose ideas often clashed with the scientific community of his era. I would love to ask him about his many uncompleted projects, his thoughts on the future of energy, wireless communication, and the challenges he faced in translating his groundbreaking ideas into practical applications. His ability to think in terms of large-scale systems—like the concept of global wireless communication—would be fascinating to discuss in the context of today’s world of interconnected networks and clean energy technology.

Tesla’s unique combination of brilliance, creativity, and struggles in gaining widespread recognition during his lifetime makes him a particularly intriguing figure to meet.

