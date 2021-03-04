'''
Created on 26 Sep. 2017

@author: gh
'''

# Advanced task (train-2.py) - George Hayward - 43692486
# Implemented additional features: 'Travel time' Function.

import datetime
from datetime import timedelta
import sqlite3
import time

# Define welcome message variable for possible reuse.
welcomeMessage = """\
Computer: Welcome to Sydney Train's text-based rail information service. 
          This service will help you finding convenient ways to travel by asking you a number of questions.
          If you are not sure about how to answer a question, simply type 'help'. You can 'quit' the conversation anytime.\n
Computer: Would you like timetable information or information on trackwork?
"""

today = "2017-09-15" # Only for assignment. Change the date to datetime.datetime.today() for ACTUAL today.

# These dictionaries will be used to hold user information and states.
trackwork = {"Selected" : False} # Will contain - "Line" : None, "Date" : None, "Time" : None
timetable = {"Selected" : False} # Will contain - "Departing" : None, "Arriving" : None, "Date" : None, "Time" : None, "Table" : None
selectedTime = {"Time" : "NONE"} # Used to keep track of the current selected time in the final state of the timetable selected.

# 'Northern line' possible grammar.
northernLine = ('northern line', 'north line', 'northernline', 'northline', 'northern', 'northern-line')

# A tuple containing all of the possible stations.
stations = ('hornsby', 'normanhurst', 'thornleigh', 'pennant hills', 'beecroft', 'cheltenham',
            'epping', 'eastwood', 'denistone', 'west ryde', 'meadowbank','rhodes', 'concord west',
            'north strathfield', 'strathfield', 'burwood', 'redfern', 'central', 'town hall', 'wynyard',
            'milsons point', 'north sydney', 'waverton', 'wollstonecraft', 'st leonards', 'artarmon', 'chatswood'
            )

# A dictionary containing all possible date grammar.
dateGrammar = {
    "01" : ('first', '1st', '01'),
    "02" : ('second', '2nd', '02'),
    "03" : ('third', '3rd', '03'),
    "04" : ('forth', '4th', '04'),
    "05" : ('fifth', '5th', '05'),
    "06" : ('sixth', '6th', '06'),
    "07" : ('seventh', '7th', '07'),
    "08" : ('eighth', '8th', '08'),
    "09" : ('ninth', '9th', '09'),
    "10" : ('tenth', '10th', '10'),
    "11" : ('eleventh', '11th', '11'),
    "12" : ('twelfth', '12th', '12'),
    "13" : ('thirteenth', '13th', '13'),
    "14" : ('fourteenth', '14th', '14'),
    "15" : ('fifteenth', '15th', '15'),
    "16" : ('sixteenth', '16th', '16'),
    "17" : ('seventeenth', '17th', '17'),
    "18" : ('eighteenth', '18th', '18'),
    "19" : ('nineteenth', '19th', '19'),
    "20" : ('twentieth', '20th', '20'),
    "21" : ('twenty first', 'twenty-first', '21st', '21'),
    "22" : ('twenty second', 'twenty-second', '22nd', '22'),
    "23" : ('twenty third', 'twenty-third', '23rd', '23'),
    "24" : ('twenty fourth', 'twenty-fourth', '24th', '24'),
    "25" : ('twenty fifth', 'twenty-fifth', '25th', '25'),
    "26" : ('twenty sixth', 'twenty-sixth', '26th', '26'),
    "27" : ('twenty seventh', 'twenty-seventh', '27th', '27'),
    "28" : ('twenty eighth', 'twenty-eighth', '28th', '28'),
    "29" : ('twenty ninth', 'twenty-ninth', '29th', '29'),
    "30" : ('thirtieth', '30th', '30'),
    "31" : ('thirty first', 'thirty-first', '31st', '31'),  
    }

# A dictionary containing all possible day grammar.
dayGrammar = {
    "Monday" : ('monday', 'mon'),
    "Tuesday" : ('tuesday', 'tue'),
    "Wednesday" : ('wednesday', 'wed'),
    "Thursday" : ('thursday', 'thur'),
    "Friday" : ('friday', 'fri'),
    "Saturday" : ('saturday', 'sat'),
    "Sunday" : ('sunday', 'sun')
    }

# This function resets the inputed dictionary as well as the selectedTime dictionary.
def resetDict(dict):
    selectedTime["Time"] = "NONE"
    dict.clear()
    dict = {"Selected" : False}

 # Returns the date of the month corresponding with the inputed day.
def getDay(aString):
    # Get tomorrows date
    iterDate = datetime.datetime.strptime(today, "%Y-%m-%d") + timedelta(days = 1)
    # Continue until return
    while True:
        # If iterDate is equal to the input day then return the first two digits of iterDate.
        if iterDate.strftime("%A") == aString:
            return iterDate.strftime("%d")
        # Otherwise go to the next day and test again.
        else:
            iterDate = iterDate + timedelta(days = 1)

# Checks to see if the input string contains a valid time. Valid time = 'HH:MM'
def validTime(aString):
    if ':' in aString:
        loc = aString.find(':')
        # Check to see if the string length is possible.
        if loc >= 2:
            if loc <= len(aString) - 3:
                # Make a substring of the time.
                theTime = aString[loc-2:loc+3]
                digitCount = 0
                # Count the amount of digits in the time.
                for x in theTime:
                    if x.isdigit():
                        digitCount += 1
                # Try to pass the string as a time.
                try:
                    time.strptime(theTime, '%H:%M')
                    # If a valid time according to the time module and contains exactly 4 digits then return True.
                    if digitCount == 4:
                        return True
                # If can't pass as a string, return false.
                except ValueError:
                    return False
    # If all conditions fail the return false.
    else: return False

# Trackwork SQL retrieval function. Retrieves information from the database based on the previously saved user input.
def trackworkQuery():
    # Turn user input into variables for substitution.
    thisLine = trackwork.get("Line")
    thisDate = trackwork.get("Date")
    thisTime = trackwork.get("Time")
    # Open the .db
    conn = sqlite3.connect('train.db')
    # Create a cursor and execute the SQL script to retrieve trackwork information. Use .format to sub in user values.
    cursor = conn.execute("""
        SELECT Line_Name, Start_Date, End_Date, Message
        FROM  Trackwork x, Line y  
        WHERE x.Line_ID = y.LINE_ID AND Line_Name = '{0}' AND
              (strftime( '%s', Start_Date ) <= strftime( '%s', '{1} {2}') AND
               strftime( '%s', End_Date )   >= strftime( '%s', '{1} {2}') )""".format(thisLine, thisDate, thisTime))
    
    # Return trackwork information if there is trackwork.
    for row in cursor:
        firstDate = datetime.datetime.strptime(row[1], "%Y-%m-%d %H:%M").strftime("%A %d %B %H:%M")
        secondDate = datetime.datetime.strptime(row[2], "%Y-%m-%d %H:%M").strftime("%A %d %B %H:%M")
        resetDict(trackwork)
        return ("There is trackwork on the {0} on {1} to {2}; {3}".format(row[0], firstDate, secondDate, row[3]) +
                "\n\nComputer: Would you like more information on timetable or trackwork?")
        
    
    # If there is no trackwork on the user input, return a message explaining so.  
    else:
        date = datetime.datetime.strptime(thisDate + " " + thisTime, "%Y-%m-%d %H:%M").strftime("%A %d %B %H:%M")
        resetDict(trackwork)
        # Close the SQL access.
        conn.close()
        return ("There is no trackwork on the {0} on {1}".format(thisLine, date) +
                "\n\nComputer: Would you like more information on timetable or trackwork?")

# Timetable SQL retrieval function. Retrieves information from the database based on the previously saved user input.
def timetableQuery():
    # Turn user input into variables for substitution
    thisDepart = timetable.get("Departing")
    thisArrive = timetable.get("Arriving")
    thisDate = timetable.get("Date")
    thisTime = timetable.get("Time")
    dayType = getDayType(thisDate)
    # Open the .db
    conn = sqlite3.connect('train.db')
    # Create a cursor and execute the SQL script to retrieve timetable information. Use .format to sub in user values.
    cursor = conn.execute("""
       SELECT '{0}', x.Departure_Time, '{1}',
              strftime('%H:%M',datetime(y.Departure_Time, '-1 minute'))  
       FROM   Schedule x, Schedule y
       WHERE  x.Station_ID = (SELECT x.Station_ID FROM Station x
                              WHERE  x.Station_Name = '{0}') 
              AND EXISTS (SELECT y.Station_ID
                          WHERE  y.Train_ID = x.Train_ID
                                 AND y.Station_ID = (SELECT Station_ID FROM Station
                                                     WHERE  Station_Name = '{1}')
                          AND  x.Departure_Time < y.Departure_Time)
              AND (SELECT Part_Of_Week
                   FROM   Train
                   WHERE  Train_ID = x.Train_ID) = '{2}'
       ORDER BY
             ABS(strftime('%s', x.Departure_Time) - strftime('%s', '{3}')) ASC;
       """.format(thisDepart, thisArrive, dayType, thisTime))
    # Create a list to hold all the returned results.
    table = []
    # Fill the table
    for row in cursor: 
        table.append(row)
    # No need for SQL access, close conn.
    conn.close()
    # Save the table into the timetable dictionary for further querys (earlier / later times). Also to put the program into its next state.
    timetable["Table"] = table
    # Save the first returned departing train time in the selectedTime dictionary.
    selectedTime["Time"] = table[0][1]
    
    # Return the next appropriate train and offer the user a earlier / later train.    
    return ("Let me see - I have a train leaving " 
            + thisDepart 
            + " at " + table[0][1] 
            + " and arriving at "  + thisArrive 
            + " at " + table[0][3] + ".\n\nComputer: Would you like an earlier/later train? Or this trips travel time?")

# Input a day string (e.g. 'Monday'). Return WE(weekend) or WD(weekday) appropriate to the day.
def getDayType(aString):
    WD = ("monday", 'tuesday', 'wednesday', 'thursday', 'friday')
    WE = ("saturday", "sunday")
    date = datetime.datetime.strptime(aString, "%Y-%m-%d").strftime("%A")
    if date.lower() in WD:
        return "WD"
    elif date.lower() in WE:
        return "WE"

# Returns the travel time of the selected trip.
def travelTime():
    # Find the correct trip tuple in the stored timetable.
    for x in timetable["Table"]:
        # If the selected time matches the timetable tuple then create timeDiff.
        if selectedTime.get("Time") == x[1]:
            # Get the difference in minutes between the arriving time and departing time.
            timeDiff = (datetime.datetime.strptime(x[3], '%H:%M') -
            datetime.datetime.strptime(selectedTime.get("Time"), '%H:%M'))
            travelMins = str(int(timeDiff.total_seconds() / 60))
            # Return the travel time.
            return ("The travel time for the selected trip from " 
                    + timetable["Departing"] 
                    + " to " 
                    + timetable["Arriving"]
                    + " is " + travelMins + " minutes."
                    + "\n\nComputer: Can I help you find a earlier or later train? or are you finished?")

# Retrieves an earlier or later time from the selected train timetable, based on the user input.
def getTime(aString):
    # If input is 'earlier'
    if aString == "earlier":
        # Create a temporary duplicate of the current selectedTime.
        earlier = selectedTime.get("Time")
        # Go through all the times in this particular timetable
        for x in timetable["Table"]:
            # If a time is less than earlier then make that time the new 'earlier'
            if datetime.datetime.strptime(x[1], '%H:%M').time() < datetime.datetime.strptime(earlier, '%H:%M').time():
                earlier = x[1]
        # If earlier == selectedTime still, there are no earlier times.
        if earlier == selectedTime.get("Time"):
           return ("The train departing from " 
                + timetable["Departing"] 
                + " at " + selectedTime.get("Time")
                + ", Appears to be the earliest train. Would you like a later train?")        
        # If an earlier time has been found, check and see if there is an earlier time which is closest to the selected time.
        else:
            for x in timetable["Table"]:
                # If the item is less than selected time.
                if datetime.datetime.strptime(x[1], '%H:%M').time() < datetime.datetime.strptime(selectedTime.get("Time"), '%H:%M').time():
                    # If the item is greater the earlier item
                    if datetime.datetime.strptime(x[1], '%H:%M').time() > datetime.datetime.strptime(earlier, '%H:%M').time():
                        # Make 'earlier' the new time to be reported.
                        earlier = x[1]
            selectedTime["Time"] = earlier
            # Find the train trip which departs at the 'earlier' time. Report the trip and offer an earlier train.
            for x in timetable["Table"]:
                if earlier in x[1]:
                    return ("Let me check - I have a train leaving " 
                            + x[0] 
                            + " at " 
                            + x[1] + " and arriving at "
                            + x[2] + " at "
                            + x[3] + ". Would you like an earlier train?")
    # Do exactly the same as above, except later rather than earlier.
    elif aString =="later":
        # Create a temporary duplicate of the current selectedTime.
        later = selectedTime.get("Time")
        # Go through all the times in this particular timetable
        for x in timetable["Table"]:
            # If a time is greater than later then make that time the new later
            if datetime.datetime.strptime(x[1], '%H:%M').time() > datetime.datetime.strptime(later, '%H:%M').time():
                later = x[1]
        # If later == selectedTime still, there are no later times.
        if later == selectedTime.get("Time"):
           return ("The train departing from " 
                + timetable["Departing"] 
                + " at " + selectedTime.get("Time")
                + ", Appears to be the latest train. Would you like a earlier train?")        
        # If a later time has been found, check and see if there is a later time which is closest to the selected time.
        else:
            for x in timetable["Table"]:
                # If the item is greater than selected time.
                if datetime.datetime.strptime(x[1], '%H:%M').time() > datetime.datetime.strptime(selectedTime.get("Time"), '%H:%M').time():
                    # If the item is less than the later item
                    if datetime.datetime.strptime(x[1], '%H:%M').time() < datetime.datetime.strptime(later, '%H:%M').time():
                        # Make 'later' the new time to be reported.
                        later = x[1]
            selectedTime["Time"] = later
            # Find the train trip which departs at the 'later' time. Report the trip and offer an later train.
            for x in timetable["Table"]:
                if later in x[1]:
                    return ("Let me check - I have a train leaving " 
                            + x[0] 
                            + " at " 
                            + x[1] + " and arriving at "
                            + x[2] + " at "
                            + x[3] + ". Would you like an later train?")

# Returns an element from the tuple that is within the string. If no element from the tuple is in the string then return "0".
def checkTuple(aTuple, aString):
    for x in aTuple:
        if x in aString.lower():
            return x
    else: return "0"

# Return the key of the dictionary which contains the inputed string. Otherwise return "0"
def checkDict(aDict, aString):
    keyList = list(aDict)
    keyList = keyList[::-1]
    for currentKey in keyList:
        # If element from tuple is found in input. Create variable based on that tuples key.
        if checkTuple(aDict[currentKey], aString) != "0":
            return currentKey
    else:
        return "0"
    
# This function finds out which response mode the program should go in to. First it checks if the user needs help or if the user input is invalid.
# Otherwise it goes on to analyze the input for a functional response.
def response(userInput):
    # Print the goodbye message if user wishes to quit out of the program
    if(userInput.lower() == "quit"):
        return "Okay, thank you for travelling with Sydney Train - cost effective, reliable and convenient."
    # User input should be at least 1 character
    elif(len(userInput) < 1):
        return "Invalid Input: User input must be at least 1 character or more."
    # Check current state and return appropriate message.
    # If user types 'help'
    elif(userInput.lower() == "help"):
        # In trackwork information state
        if(trackwork.get("Selected") == True):
            dictSize = len(trackwork)
            # Line State
            if(dictSize == 2):
                return "You have selected trackwork information. Please let us know which train line you wish to travel on. E.g (Northern Line)"
            # Date State
            elif(dictSize == 3):
                return ("You have selected to travel on the " + trackwork.get("Line") 
                        + ". Please input the date you wish to travel on. E.g (The Sixteenth)")
            # Time State
            elif(dictSize == 4):
                return ("You have selected to travel on the " + trackwork.get("Line") 
                        + " on the " + trackwork.get("Date") 
                        + ". Please input the time you wish to travel on. E.g (10:00)")
        # In timetable information state
        elif(timetable.get("Selected") == True):
            dictSize = len(timetable)
            # Departing state
            if(dictSize == 2):
                return "You have selected timetable information. Please let us know which station you would like to depart from. E.g (Central Station)"
            # Arriving state
            elif(dictSize == 3):
                return ("You have selected to depart from " + timetable.get("Departing")
                        + ". Please input the station that you would like to arrive at. E.g (Epping)")
            # Date State
            elif(dictSize == 4):
                return ("You have selected to travel from " 
                        + timetable.get("Departing") 
                        + " to " + timetable.get("Arriving") 
                        + ". Please input the date that you wish to travel on. E.g (The Sixteenth)")
            # Time State
            elif(dictSize == 5):
                date = datetime.datetime.strptime(timetable['Date'], "%Y-%m-%d").strftime("%A, %B %d")
                return ("You have selected to travel from " 
                        + timetable.get("Departing") 
                        + " to " + timetable.get("Arriving") 
                        + " on " + date + ". Please input the time that you wish to travel on. E.g (10:00)")     
            # Table State
            elif(dictSize == 6):
                date = datetime.datetime.strptime(timetable['Date'], "%Y-%m-%d").strftime("%A, %B %d")
                return ("You have selected to travel from " 
                        + timetable.get("Departing") 
                        + " to " + timetable.get("Arriving") 
                        + " on " + date + " at " +
                        selectedTime.get("Time") 
                        + ".\n\nComputer: Please type in 'earlier' or 'later' for an earlier or later train. Or type 'Travel time' to get this trips travel time. Or simply type 'no' if you are finished."
                )  
        # First phase (trackwork | timetable)
        else:
            return "This service can provide information on train timetable or trackwork information. Please type either timetable or trackwork."
    # Go on the analyze phase.
    else: return analyze(userInput)

# This function reads the user input and current program state. It then asks questions depending on that state for the ultimate goal of retrieving information for the user.
def analyze(userInput):
    # If the word 'timetable' is in the user input then mark the timetable dictionary as selected and deselect the trackwork dictionary.
    if 'timetable' in userInput.lower():
        timetable["Selected"] = True
        resetDict(trackwork)
        
    # Same as above with 'trackwork' user input.
    elif 'trackwork' in userInput.lower():
        trackwork["Selected"] = True
        resetDict(timetable)
        
    # If the user has selected trackwork
    if(trackwork.get("Selected") == True):
        # Get the size of the dictionary so the program knows what state the user is in.
        dictSize = len(trackwork)
        
        # Allocate line input to dictionary.
        if(dictSize == 2):
            #go through northernLine tuple
            if checkTuple(northernLine, userInput) != "0":
                trackwork['Line'] = 'Northern Line'
            else:
                return "Please input the train line that you are planning to travel on."
        # Allocate the date input to dictionary.    
        elif(dictSize == 3):
            # If the user wants timetables for today.
            if 'today' in userInput.lower():    
                trackwork['Date'] = today
            # If the user wants timetables for tomorrow.
            elif 'tomorrow' in userInput.lower():
                trackwork['Date'] = "2017-09-16" # Only for assignment. Change to datetime.datetime.today() + datetime.timedelta(days=1) for ACTUAL tomorrow.
            # User wants a specific date.
            elif checkDict(dateGrammar, userInput) != "0":
                str = checkDict(dateGrammar, userInput)
                trackwork['Date'] = "2017-09-{}".format(str)
            # User wants a day relative to today's day.
            elif checkDict(dayGrammar, userInput) != "0":
                str = checkDict(dayGrammar, userInput)
                str = getDay(str)
                trackwork['Date'] = "2017-09-{}".format(str)   
            # No date grammar found in user input.
            else:
                return "Please input the date you will be travelling on."

        # Allocate the time input to dictionary.    
        elif(dictSize == 4):
            # User wants time in HH:
            if validTime(userInput):
                index = userInput.find(':')
                # Make a string of the inputed time.
                timeString = userInput[index - 2] + userInput[index - 1] + userInput[index] + userInput[index + 1] + userInput[index + 2]
                trackwork['Time'] = timeString
            # No time grammar found.
            else:
                return "Please enter a valid time"
        
        # Return next step to the user update dictionary size depending on that step.
        # If the user has inputed: (Line, Date, Time)
        if(dictSize == 4):
            # Run the trackwork query.
            return trackworkQuery()
        
        # If the user has inputted: (Line, Date)
        elif(dictSize == 3):
            trackwork['Time'] = None
            return "At what time?"
        
        # If the user has inputted: (Line)
        elif(dictSize == 2):
            trackwork['Date'] = None
            return "On what day will you be travelling?"
        
         # If the user has only just selected trackwork.
        else: 
            trackwork['Line'] = None
            return "On what line are you planning to travel on?"

       
    # If the user has selected timetable
    elif(timetable.get("Selected") == True):
        dictSize = len(timetable)
        # Find departing station
        if (dictSize == 2):
            # Look for a match of a departing station in the input.
            if checkTuple(stations, userInput) != "0":
                timetable['Departing'] = checkTuple(stations, userInput).title()
            # No match found
            else:
                return "Please input the train station you would like to leave from."
        # Find arriving station
        elif (dictSize == 3):
            # Look for a match of an arriving station in the input.
            if checkTuple(stations, userInput) != "0":
                if checkTuple(stations, userInput) != timetable.get('Departing'):
                    timetable['Arriving'] = checkTuple(stations, userInput).title()
                else:
                    return "You can't depart and arrive at the same station."
            # No match found
            else:
                return "Please input the train station you would like to leave from."
            
        # Get the travelling date
        elif (dictSize == 4):
            # If the user wants timetables for today.
            if 'today' in userInput.lower():
                timetable['Date'] = today
            # If the user wants timetables for tomorrow.
            elif 'tomorrow' in userInput.lower():
                timetable['Date'] = "2017-09-16" # Only for assignment. Change to datetime.datetime.today() + datetime.timedelta(days=1) for ACTUAL tomorrow.
            # User wants a specific date.
            elif checkDict(dateGrammar, userInput) != "0":
                str = checkDict(dateGrammar, userInput)
                timetable['Date'] = "2017-09-{}".format(str)
            # User wants a day relative to today's day.
            elif checkDict(dayGrammar, userInput) != "0":
                str = checkDict(dayGrammar, userInput)
                str = getDay(str)
                timetable['Date'] = "2017-09-{}".format(str)   
            # No date grammar found in user input.
            else:
                return "Please input the date you will be travelling on."
            
        # Get the travelling time
        elif (dictSize == 5):
            # User wants time in HH:
            if validTime(userInput):
                index = userInput.find(':')
                timeString = userInput[index - 2] + userInput[index - 1] + userInput[index] + userInput[index + 1] + userInput[index + 2]
                timetable['Time'] = timeString
            # No time grammar found
            else:
                return "Please enter a valid time"
        
        # Get an earlier or later train
        elif (dictSize == 6):
            # If user wants later train
            if "later" in userInput.lower():
                return getTime("later")
            # If user wants earlier train
            elif "earlier" in userInput.lower():
                return getTime("earlier")
            # If user is finished
            elif "no" in userInput.lower():
                resetDict(timetable)
                print("Computer: Okay, thank you for travelling with Sydney Train - cost effective, reliable and convenient.")
                exit()
            elif "travel time" in userInput.lower():
                return travelTime()
            # Not recognised grammar
            else:
                return "Please enter 'later' if you want an later train or 'earlier' if you want an earlier train. \n\nComputer: Or type 'quit' if you are finished."
        
        # If the user has inputted: (Departing station, arriving station, Travel Date, Travel Time)
        # Return timetable information
        if (dictSize == 5):
            return timetableQuery()
        # If the user has inputted: (Departing station, arriving station, Travel Date)
        elif (dictSize == 4):
            timetable['Time'] = None
            date = datetime.datetime.strptime(timetable['Date'], "%Y-%m-%d").strftime("%A, %B %d,")
            return "Travelling on " + date + " and what time would you like to depart from " + timetable.get("Departing") + "?"
        # If the user has inputted: (Departing station, arriving station)
        elif (dictSize == 3):
            timetable['Date'] = None
            return "Okay, to " + timetable.get('Arriving') + " and do you want to travel on a weekday or on a weekend?"
        # If the user has inputted: (Departing station)
        elif (dictSize == 2):
            timetable['Arriving'] = None
            return "Okay, " + timetable.get("Departing") + ". And where are you travelling to?"
        # If the user has only just selected timetable.
        else:
            timetable['Departing'] = None
            return "Sure. What station would you like to leave from?"
    

    # If neither option has been selected, prompt the user again.
    else:
        return "Please type either 'timetable' or 'trackwork'"
        
# Start program
def main():
    
    # Welcome message / Initial instruction
    print()
    print(welcomeMessage)
    
    # While loop which will run until the user quit's out of the program. This will continually trigger the automatic response system.
    while True:
        # User input
        statement = input("User:     ")
        
        # Analyze user input and respond appropriately.
        print() #spaces for readability
        print("Computer: "+ response(statement))
        print()
        
        # Quit out of the program
        if statement == "quit":
            break #Stop loop

if __name__ == '__main__': main()
