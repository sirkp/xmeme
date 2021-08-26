#!/bin/bash

mongoimport --db greetings --collection memes --drop --jsonArray --file ./memes-data.json