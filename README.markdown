A [Giter8][g8] template for the official Colossus quickstart example project with Redis and Memcached support.

Usage
-
***The example assumes that you have Redis (port 6379) and Memcached (port 11211) running on your local machine.***

	$ sbt new cuno/colossus-quickstart.g8

Your new project will have the [sbt-revolver](https://github.com/spray/sbt-revolver)  plugin added to it, you can use the `~re-start` command to run the application in "triggered restart" mode.

	$ sbt
	...
	> ~reStart
You should now be able to send requests to it E.G.

	$ curl --data '{ "value" : "Hello world!" }' http://localhost:9000/set/mykey
	$ curl -X GET http://localhost:9000/get/mykey

Template license
-
Written in 2017 by Cuno de Boer cuno.de.boer@gmail.com

Based on http://tumblr.github.io/colossus/docs/quickstart

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/
