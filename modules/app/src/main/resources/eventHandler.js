(function processEvent(aggregate, event) {
    console.log(aggregate, event)
    // let evt = JSON.parse(event.value())
    console.log(event.aggregateId())
    console.log(event.value())
    const evt = JSON.parse(event.value())
    if (evt.kind == "BookCreated")
        return JSON.stringify({ aggregateId: evt.id, value: { title: evt.title, author: evt.author } })
    else if (evt.kind == "BookUpdated") {
        const oldData = JSON.parse(aggregate.value())
        return JSON.stringify({ aggregateId: aggregate.aggregateId(), value: { title: evt.title || oldData.title, author: evt.author || oldData.author } })
    }
    else return JSON.stringify({ "aggregateId": "someId", "value": {} })
})