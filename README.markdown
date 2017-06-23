A [Giter8][g8] template for the official Colossus quickstart example project with Redis and Memcached support.

Requirements
-
The example assumes that you have running on your local machine:
- Redis (port 6379)
- Memcached (port 11211)

Running
-
The [sbt-revolver](https://github.com/spray/sbt-revolver)  plugin was added to the project. In sbt's interactive mode use the following command:

	> ~reStart
That will run the application in "triggered restart" mode. You should now be able to send requests to it E.G.

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
