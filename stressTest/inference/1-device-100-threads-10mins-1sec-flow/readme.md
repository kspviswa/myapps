# Base Data
10mins -> 600 sec
100 threads -> flow mods with 1 sec interval

Total flows : 26,235

## Util commands
=============

Total flow counts : `grep "#### Installed flow : Success ops" *.log* | wc -l`

Get timestamp & flow id:

`grep "Starting the Flowpusher task for org.test.app" * | awk '{print $2, $27}' > flow_started.csv`

`grep "#### Installed flow : Success ops" * | awk '{print $2, $23}' > flow_success.csv`
