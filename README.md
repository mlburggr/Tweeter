# Tweeter
Tweeter is a game based on Conway’s Game of Life that focuses on the population and evolution of songbirds as opposed to cellular automation in Conway’s Game. This project is developed by Nicholas Walker (Team Leader/Method Programmer), Maxwell Burggraf (Method Programmer), and Calvin He (Architect Programmer). 

By using the Java language in the Eclipse IDE, this project has imported Slick2D, a Lightweight Java Game Library, to design graphics and develop the gameplay. JSyn, an audio synthesis software API for Java, is another essential package to create and generate sound effects.

Like Conway’s Game, Tweeter consist of a two-dimensional grid that is occupied by birds with their own song, or tweet. Users will be able to select the size of the grid, the number of non-player birds, and the amount of energy to expend. The player can control a user bird on the grid and create a customizable tweet. Once created, the tweet will be played audibly to the player using algorithms implemented from the JSyn library. 

Every non-player bird will audibly play their own individualized tweet and then listen to tweets from other birds, including the user bird. The listening process allows the bird to learn from the tweet it heard and adapt its own tweet to be more similar. Based on the similarity of the tweet it heard to its own, birds can transition into a mate, attack, or neutral mode. Birds with similar tweets are likely to mate, and very different tweets cause aggression. Mating with another bird in mate mode enables the spawn of a new bird with a new tweet resulting from the combination of the parent tweets. Attacking another bird in attack mode will result in loss of health points. Birds will then start the process all over again by tweeting and listening.

Tweeter simulates the growth and regulation of the population from the births of newborn birds with unique tweets and the death of those that do not fit in with the rest, and the player will be able to observe the evolution of tweets through generations of birds.

