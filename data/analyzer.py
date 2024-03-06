import json
import pandas as pd
import plotly.express as px


if __name__ == "__main__":
    versions = ["v1", "v2", "v3", "v4"]
    df = pd.DataFrame(columns=["n", "time", "version"])
    for version in versions:
        with open(f"data/{version}_results.json") as f:
            data = json.load(f)
            data = pd.DataFrame(data)
            data = data[data["success"]]
            data = data[data["time"] < data["time"].quantile(0.95)]
            data["version"] = version

            # append to df
            df = pd.concat([df, data], ignore_index=True)


    # create n versus time plot
    px.scatter(df, x="n", y="time", color="version", title="Time vs N").show()
    

