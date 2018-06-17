# webscraper
Generate a sitemap of URLs on a given website

## Overview

Download from the git repository. 
The code is a Maven project, so you can build the executable Jar and run that way if you prefer. 
However, you can run directly in your IDE (I used NetBeans for this project). 

## Instruction
In the Source Packages "scraper" folder the DoScrape class has the Main method. Run this and the scrape will commence. 
On my machine the process takes about 1 minute. 

The output is both printed on the console and into a file, sitemap.xml. 
In the console you can see an exception for https://wiprodigital.com/cases/case-study-esab/ 

## Thought process
The thought process was simple enough 
1) Use Jsoup to obtain each element with an attribute containing URLs from a given page
2) Do this recursively for each URL obtained, only for URLs that start with https://wiprodigital.com/
3) Use JAXB to Marshal the obtained URLs into an XML format

## Possible Adjustments
Set URL input to be read from a database so that different websites could be entered.
For each entry in the database run the scrape and follow each URL that starts with input provided. 

Consider a multithreaded implementation. Speed is not prohibitive, but it would be worth trying to speed up.
I'd like to use the executor service to give each task of scraping a URL, its own thread. 
However, given that the task is done recursively and with a global variable (to store all URLs found so far), 
the implementation would need to be well considered to avoid potential issues.
