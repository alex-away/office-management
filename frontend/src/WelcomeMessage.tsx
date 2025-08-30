function WelcomeMessage() {

    const userName: string = "Alice";
    const greeting: string  = "Hello";

    return (
        <div>
            <h1>{greeting}, {userName}</h1>
            <p>Welcome to your first React app.</p>
        </div>
    )
}

export default WelcomeMessage;