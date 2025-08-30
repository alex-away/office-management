function Message2() {
    const name = "me";
    // jsx only returns a single Element, so we use fragments to return multiple
    return (
        <>
            <h1>hello {name}</h1>
            <h2>hello world</h2>
        </>
    )
}

export default Message2;