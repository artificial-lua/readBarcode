import requests
import json

f = open('Tester/config.json', 'rt', encoding='UTF-8')

config = json.load(f)

host = config['host'] + ":" + config['port']
path = config['path']

params = config['params']

while True:
    for i in range(0, len(path)):
        print("%2d::[%s]"%(i + 1, path[i]))

    print()
    ch = int(input(">>"))

    headers = {'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0'}
    print(headers)
    url = host + path[ch - 1]
    print(url)
    if len(params) >= ch:
        response = requests.get(url, params=params[ch - 1], headers=headers)
    else:
        response = requests.get(url, headers=headers)

    print()
    print(response.url)
    print(response.text)
    print()
    input("Enter to reset")