// TODO add a UUID function

(function processCommand(aggregate, command) {
    console.log(aggregate, command)
    // console.log(command.id())
    //return "hello"
    const cmd = JSON.parse(command.value())
    // console.log(cmd.action)
    // console.log(cmd.data.title)
    // console.log(cmd.data.author)

    let events = []

    const entityId = cmd.data.id || "aagasdvasd"

    if (cmd.action == 'CreateBook') {
        events.push({ "aggregateId": "abc", "value": { kind: "BookCreated", id: entityId, title: cmd.data.title, author: cmd.data.author } })
        // events.push({ "aggregateId": "abc", "value": `{ "kind:" "BookCreated", "title": cmd.data.title, "author": cmd.data.author }` })
    }
    else if (cmd.action == 'UpdateBook') {
        events.push({ "aggregateId": "def", "value": { kind: "BookUpdated", id: aggregate.aggregateId(), title: cmd.data.title } })
    }
    // else {
    //     events.concat([{ "aggregateId": "abc", "value": "bar" }, { "aggregateId": "def", "value": "baz" }])
    // }

    console.log(JSON.stringify(events))
    return JSON.stringify(events)
})