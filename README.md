# FrameAppender
Takes the output of the Hybond program and appends the wanted data to a new file in a csv format. This enables better post processing of the Hybond output.

__Usage:__
+ Argument 1 = filename without the frame number in it
+ Argument 2 = Number of files that need to be processed (number of frames from the simulation)
+ Argument 3 = Line number within the .csv files being processed that holds the value wanted
+ Argument 4 = Name of the output file
+ Argument 5 = The type of file being read (options are)
    * m - Minus interactions (attractive)
    * p - Positive interactions (repulsive)
    * t - Total interaction (Sum of minus and positive interactions)
