# Software-Engineering-Spring-2024
Project for a Software Engineering course at SUNY New Paltz
By: Nathan Gopee, CJ Jenks, and Mike Manzi

## Table of Contents
- [Project Description](#project-description)
- [System Diagram](#system-diagram)
- [Example Input](#example-input)
- [Example Output](#example-output)
- [Prototype Code](#prototype-code)

## Project Description
This project is a web application that will generate a fractal image based on the Fibonacci sequence. The user will input a number, and the application will generate a fractal construction data and raw image based on the Fibonacci sequence up to that number. Clientside, the application will allow for scaling and panning of the Fibonacci fractal.

## Example Fractal Spiral(from prototype code)
![image](fibonacci_fractal_legacy.png)

## System Diagram
![image](https://github.com/ndg8743/Software-Engineering-Spring-2024/assets/73437724/515e890f-0d90-4f22-86fb-5e955e6435e7)

## Example Input
```javascript
// Input is always primed with a json object discussing payload and output types and data
// Output is always a json object with the payload data and output data

// Example inputs:
{
  inputType: 'json', //  'csv', json, file, database, ...
  delimiter: ';', // if applicable. ignored if not
  payloadData: "{maxElement: 20}", // is a path to a file, a json object, a csv string, a database connection, ...
  outputType: 'payload', // 'csv', payload, file, database, ...
  outputData: "" // is a path to a file, a json object, a csv string, a database connection, ...
}

{
  inputType: 'csv', //  'csv', json, file, database, ...
  delimiter: ';', // if applicable. ignored if not
  payloadData: "20", // is a path to a file, a json object, a csv string, a database connection, ...
  outputType: 'csv', // 'csv', payload, file, database, ...
  outputData: "" // is a path to a file, a json object, a csv string, a database connection, ...
}
```
## Example Output
```javascript
// Example outputs:
{
  outputType: "json", // type of payload data
  payloadData: {
    maxElement: 20,
    fibSequence:  [1, 1, 2, 3, 5, 8, ...], // fib sequence
    rectSequence: [[0,0], [0,1], [-1,-2], ...], // fractal rectangle offsets
    pngBase64:    "base64encodedstring", // image of fractal
  }
}
{
  outputType: "csv", // type of payload data
  payloadData: "1,1,2,3,5,8,...;[0,0], [0,1], [-1,-2],...;base64encodedstring", // csv string
}
```
=======
## Prototype Code
[Prototype Code](/src/main/java/org/legacy/)
Written without api. Quick and dirty.
=======
