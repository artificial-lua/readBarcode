import requests
import json

while True:

    f = open('Tester/config.json', 'rt', encoding='UTF-8')

    config = json.load(f)

    host = config['host'] + ":" + config['port']
    path = config['path']

    params = config['params']

    for i in range(0, len(path)):
        print("%2d::[%s]"%(i + 1, path[i]))

    print()
    ch = input(">>")
    if (ch.isdigit()):
        ch = int(ch)
        if (ch <= len(path)):
            headers = {'User-Agent' : 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101 Firefox/91.0'}
            print(headers)
            url = host + path[ch - 1]
            print(url)
            if len(params) >= ch:
                response = requests.get(url, params=params[ch - 1], headers=headers)
            else:
                response = requests.get(url, headers=headers)
            print(response.url)
            print()
            print(response.text)
            print()
    input("Enter to reset")