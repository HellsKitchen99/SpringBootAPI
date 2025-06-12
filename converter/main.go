package main

import (
	"encoding/json"
	"fmt"
	"os"
)

func main() {
	data, err := os.ReadFile("data.json")
	if err != nil {
		fmt.Println("ошибка при чтении файла -", err)
		return
	}
	var docs []map[string]interface{}
	errUnmarshal := json.Unmarshal(data, &docs)
	if errUnmarshal != nil {
		fmt.Println("ошибка при парсинге данных -", errUnmarshal)
		return
	}

	file, errCreate := os.Create("bulk.ndjson")
	if errCreate != nil {
		fmt.Println("ошибка создания файла -", errCreate)
		return
	}
	defer file.Close()

	for _, doc := range docs {
		meta := `{"index":{}}`
		docBytes, err := json.Marshal(doc)
		if err != nil {
			fmt.Println("ошибка парсинга структуры -", err)
			return
		}
		_, errWriteMeta := file.WriteString(meta + "\n")
		if errWriteMeta != nil {
			fmt.Println("ошибка записи строки в файл -", errWriteMeta)
			return
		}
		_, errWriteDoc := file.WriteString(string(docBytes) + "\n")
		if errWriteDoc != nil {
			fmt.Println("ошибка записи строки в файл -", errWriteDoc)
			return
		}
	}
	fmt.Println("успешно")
}
