import * as React from "react";

import { TyloFloatingBubbleViewProps } from "./TyloFloatingBubble.types";

export default function TyloFloatingBubbleView(
  props: TyloFloatingBubbleViewProps
) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad && props.onLoad()}
      />
    </div>
  );
}
