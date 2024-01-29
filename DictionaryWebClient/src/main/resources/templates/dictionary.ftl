
<#import "_layout.ftl" as layout />
<@layout.header>
    <div>
        <h3>Translator</h3>
        <form action="/translator" method="post">
            <p>
                <input type="text" name="english" placeholder="type some things" value="${word.english}">
            </p>
            <h5 style="color: darkred">${error}</h5>
            <p>
                <input type="submit" value="translate to German">
            </p>
        </form>
        <p>
            <textarea disabled name="german">${word.german}</textarea>
        </p>
    </div>
</@layout.header>
