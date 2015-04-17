#!/usr/bin/env python

# This tools is made to run legacy PlayOnLinux v4 scripts
import os
import subprocess
from SetupWindow.SetupWindowNetcatServer import SetupWindowNetcatServer
from scripts import EnvironementHelper

if __name__ == '__main__':
    setupWindowNetcatServer = SetupWindowNetcatServer()
    os.environ["PLAYONLINUX"] = os.path.join(os.path.dirname(__file__), "..", "bash")
    os.environ["POL_PORT"] = str(setupWindowNetcatServer.getPort())
    os.environ["POL_COOKIE"] = setupWindowNetcatServer.getCookie()
    os.environ["POL_OS"] = EnvironementHelper.getOperatinSystem().fetchShortName()

    setupWindowNetcatServer.initServer()

    process = subprocess.call(["bash", __scriptToWrap__])

    setupWindowNetcatServer.closeServer()

