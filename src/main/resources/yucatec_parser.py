# -*- coding: utf-8 -*-

import re
import codecs
import json

# loads a dictionary from the file
def dict_from_file(dictname):
    yuc_dict = {}
    f = codecs.open(dictname, 'r', encoding='utf-8')
    for line in f:
        entry = []
        entry = line.split('\t')
        entry[-1] = entry[-1].replace('\r\n', '')
        if entry[0] in yuc_dict:
            i = 2
            word = entry[0]+str(i)
            while word in yuc_dict:
                i += 1
                word = entry[0]+str(i)
            yuc_dict[word] = entry[1:]
            yuc_dict[entry[0]+'1'] = yuc_dict[entry[0]]
            del yuc_dict[entry[0]]
        else:
            yuc_dict[entry[0]] = entry[1:]
    f.close()
    return yuc_dict

# loads a list of affixes from the file
def affixes(filename):
    prefixes = {}
    suffixes = {}
    f = codecs.open(filename, 'r', encoding='utf-8')
    for line in f:
        entry = line.split('\t')
        entry[-1] = entry[-1].replace('\r\n', '')
        aff = entry[0].replace('+', '')
        if (entry[0].endswith('+') == True):
            if aff in prefixes:
                i = 2
                affix = aff+str(i)
                while affix in prefixes:
                    i += 1
                    affix = aff+str(i)
                prefixes[affix] = entry[1:]
                prefixes[aff+'1'] = prefixes[aff]
                del prefixes[aff]
            else:
                prefixes[aff] = entry[1:]
        else:
            if aff in suffixes:
                i = 2
                affix = aff+str(i)
                while affix in suffixes:
                    i += 1
                    affix = aff+str(i)
                suffixes[affix] = entry[1:]
                suffixes[aff+'1'] = suffixes[aff]
                del suffixes[aff]
            else:
                suffixes[aff] = entry[1:]
    return prefixes, suffixes

# parses a single word
def parse(word):
    parsings = {}
    hypos = {}
    var = 1
    for rooty in yuc_dict:
        root = re.sub('[0-9]', '', rooty)
        if (word.startswith(root) == True):
            if (len(word) == len(root)):
                hypos[word+str(var)] = (rooty)
            else:
                linesuf = word[len(root):]
                for suffix in suffixes:
                    var += 1
                    suf = re.sub('[0-9]', '', suffix)
                    if (linesuf.startswith(suf) == True):
                        if (len(linesuf) == len(suf)):
                            hypos[word+str(var)] = (rooty+'+'+suffix)
                        else:
                            linesuf2 = linesuf[len(suf):]
                            for suffix2 in suffixes:
                                var += 1
                                suf = re.sub('[0-9]', '', suffix2)
                                if (linesuf2.startswith(suf) == True):
                                    if (len(linesuf2) == len(suf)):
                                        hypos[word+str(var)] = (rooty+'+'+suffix+'+'+suffix2)
                                    else:
                                        linesuf3 = linesuf2[len(suf):]
                                        for suffix3 in suffixes:
                                            var += 1
                                            suf = re.sub('[0-9]', '', suffix3)
                                            if (linesuf3.startswith(suf) == True):
                                                if (len(linesuf3) == len(suf)):
                                                    hypos[word+str(var)] = (rooty+'+'+suffix+'+'+suffix2+'+'+suffix3)
                                                else:
                                                    linesuf4 = linesuf3[len(suf):]
                                                    for suffix4 in suffixes:
                                                        var += 1
                                                        suf = re.sub('[0-9]', '', suffix4)
                                                        if (linesuf4.startswith(suf) == True):
                                                            if (len(linesuf4) == len(suf)):
                                                                hypos[word+str(var)] = (rooty+'+'+suffix+'+'+suffix2+'+'+suffix3+'+'+suffix4)

    var += 1
    for prefy in prefixes:
        pref = re.sub('[0-9]', '', prefy)
        if (word.startswith(pref) == True):
            if (len(word) > len(pref)):
                linesuf = word[len(pref):]
                for rooty in yuc_dict:
                    root = re.sub('[0-9]', '', rooty)
                    if (linesuf.startswith(root) == True):
                        if (len(linesuf) == len(root)):
                            hypos[root+str(var)] = (prefy+'+'+rooty)
                        else:
                            linesuf2 = linesuf[len(root):]
                            for suffix in suffixes:
                                var += 1
                                suf = re.sub('[0-9]', '', suffix)
                                if (linesuf2.startswith(suf) == True):
                                    if (len(linesuf2) == len(suf)):
                                        hypos[word+str(var)] = (prefy+'+'+rooty+'+'+suffix)
                                    else:
                                        linesuf3 = linesuf2[len(suf):]
                                        for suffix2 in suffixes:
                                            var += 1
                                            suf = re.sub('[0-9]', '', suffix2)
                                            if (linesuf3.startswith(suf) == True):
                                                if (len(linesuf3) == len(suf)):
                                                    hypos[word+str(var)] = (prefy+'+'+rooty+'+'+suffix+'+'+suffix2)
                                                else:
                                                    linesuf4 = linesuf3[len(suf):]
                                                    for suffix3 in suffixes:
                                                        var += 1
                                                        suf = re.sub('[0-9]', '', suffix3)
                                                        if (linesuf4.startswith(suf) == True):
                                                            if (len(linesuf3) == len(suf)):
                                                                hypos[word+str(var)] = (prefy+'+'+rooty+'+'+suffix+'+'+suffix2+'+'+suffix3)

    for key in hypos:
        root = re.sub('[0-9]', '', key)
        glosses = ' = '
        pos = 'NVIVTADJADVPRONPREPPARTCLFCONJ'
        rootex = False
        prefex = False
        list_morf = hypos[key].split('+')
        for morf in list_morf:
            root2 = re.sub('[0-9]', '', morf)
            if (morf in prefixes) and (prefex == False):
                glosses += prefixes[morf][0] + '+'
                pos += '+' + prefixes[morf][1] + '+'
                prefex = True
            elif (morf in yuc_dict) and (rootex == False):
                glo, tab, part = yuc_dict[morf][1].partition(':')
                glosses += glo + '[' + yuc_dict[morf][0] + ']'
                pos += '+' + yuc_dict[morf][0]
                rootex = True
            else:
                if morf in suffixes:
                    glosses += '+' + suffixes[morf][0]
                    pos += '+' + suffixes[morf][1]
        pos = pos.replace('++', '+')
        hypos[key] = re.sub('[0-9]', '', hypos[key])
        hypos[key] += glosses.replace(' +', ' ')

        list_pos = pos.split('+')
        flag = False
        for i in ('N', 'VI', 'VT', 'ADJ', 'ADV', 'PRON', 'PREP', 'PART', 'CLF', 'CONJ'):
            f = True
            for k in list_pos:
                if k.find(i) == -1: f = False
            if f == True: flag = True
        if flag == False:
            hypos[key] = 'DEL'

#        if glosses.count('POSS') > 2:
#            parsings[key] = 'DEL'
#        if (glosses.count('FEM+') + glosses.count('PFV')) > 1:
#            parsings[key] = 'DEL'
#        if (glosses.count('INC') + glosses.count('IMP')) > 1:
#            parsings[key] = 'DEL'
#        if (glosses.count('IMP') + glosses.count('COM')) > 1:
#            parsings[key] = 'DEL'
#        if glosses.count('IMP') > 1:
#            parsings[key] = 'DEL'
#        if glosses.count('INC') > 1:
#            parsings[key] = 'DEL'
#        if glosses.count('COM') > 1:
#            parsings[key] = 'DEL'
#        if glosses.count('PASS') > 1:
#            parsings[key] = 'DEL'
#        if glosses.count('VBLZ+ADJZ') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('VBLZ+ACU') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('ADJZ+ADJZ') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('DAT+ADJZ') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('ADJZ+ACU') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('DAT+ACU') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('EVID+PAST') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('FUT+CAUS') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('SG+CAUS') > 0:
#            hypos[key] = 'DEL'
#        if glosses.count('SG+REP') > 0:
#            hypos[key] = 'DEL'
        if glosses.count('++') > 0:
            hypos[key] = 'DEL'
        if glosses.count('[') == 0:
            hypos[key] = 'DEL'
        if glosses.count('ENCL+') > 0:
            hypos[key] = 'DEL'

    parsings = hypos.copy()
    for key in parsings:
        if parsings[key] == 'DEL':
            del hypos[key]

    for key in hypos:
        yuc, part, glosses = hypos[key].partition(' = ')
        list_gloss = glosses.split('+')
        list_yuc = yuc.split('+')
        for i in range(0,len(list_gloss)):
            if '[' in list_gloss[i]: list_yuc[i] = '<'+list_yuc[i]+'>'
        yuc = '+'.join(list_yuc)
        hypos[key] = yuc + ' = ' + glosses

    return hypos

def process_text(textname):
    f = codecs.open(textname, 'r', encoding='utf-8')
    filename, txt = textname.split('.')
    filename += '.prs'
    f2 = codecs.open(filename, 'w', encoding='utf-8')
    n_sent = 0
    parsings = {}
    for line in f:
        if line.startswith('                <ANNOTATION_VALUE>') == True:
            n_sent += 1
            item0 = line.replace('\r\n', '')
            item1 = item0.replace('                <ANNOTATION_VALUE>', '')
            item = item1.replace('</ANNOTATION_VALUE>', '')
#           speaker, tab, sentence = item.partition(']')
#           sentence0 = sentence.lower()
            sentence0 = item.lower()
            sentence0 = re.sub(u'[^a-z üáéíóúñ\'-]' ,'' , sentence0)
            sentence0 = sentence0.replace('  ', ' ')
            sentence0 = sentence0.strip()
            words = sentence0.split(' ')
            num = 0
            entry_json = {
                "id": n_sent,
                "word": item,
                "steps": []
            }

            for word in words:
                num += 1
#               if word == "yo": parsings = {"yo1": "y+<o> = 3A+want[VT]"}
#               elif word == "xi": parsings = {"xi1": "<x>+i = go[VI]+COM"}
#               elif word == "xe": parsings = {"xe1": "<x>+e = go[VI]+INC"}
#               elif word == "te": parsings = {"te1": "<t>+e = come[VI]+COM"}
#               elif word == "wo": parsings = {"wo1": "w+<o> = 2A+want[VT]"}
#               else: parsings = parse(word)
                parsings = parse(word)
                entry = str(n_sent) + '\t' + str(num) + '\t' + item + '\t' + word

                entry_json_step = {
                    "num": num,
                    "word": word,
                    "parsing": []
                }

                if len(parsings) > 1:
                    entry += '\t'

                for key in parsings:
                    entry += '\t' + parsings[key]
                    entry_json_step['parsing'].append(parsings[key])

                entry_json['steps'].append(entry_json_step)
                #print(entry_json)
                f2.write(entry + '\r\n')
            
            print(json.dumps(entry_json))

    f.close()
    f2.close()


if __name__ == '__main__':
    import sys
    file_path = "/home/alejandro/Documentos/projects/infokab/src/main/resources/"
    file_path = sys.argv[1]+"/"

    if len(sys.argv) >= 2:
#        textname = sys.argv[1]
        yuc_dict = dict_from_file(file_path+'yuc_dict.txt')
        prefixes, suffixes = affixes(file_path+'yuc_aff.txt')
        textname = file_path+'Prueba001.txt'
        process_text(textname)
#        suffixes = affixes('yuc_aff.txt')
#        process_text(textname)
    else:
        yuc_dict = dict_from_file(file_path+'yuc_dict.txt')
        prefixes, suffixes = affixes(file_path+'yuc_aff.txt')
#        suffixes = affixes('yuc_aff.txt')
        textname = file_path+'Prueba001.txt'
        process_text(textname)
#        print "Usage: kichepa.py <filename>"
