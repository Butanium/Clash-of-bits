import {api as entityModule} from '../entity-module/GraphicEntityModule.js'

export class FollowEntityModule {
    constructor(assets) {
        FollowEntityModule.instance = this
        this.previousFrame = {
            registered: new Map()
        }
        this.previousUpdateData = this.currentUpdateFrame = this.currentUpdateProgress = undefined

    }

    static get name() {
        return 'f'
    }


    updateScene(previousData, currentData, progress) {
        this.currentUpdateFrame = currentData
        this.currentUpdateProgress = progress
        this.previousUpdateData = previousData
        const registered = currentData.registered
        for (const k of registered.keys()) {
            const entity = entityModule.entities.get(k)
            const instruction = registered.get(k)
            const to_follow = entityModule.entities.get(instruction[0])
            const offsetx = instruction[1]
            const offsety = instruction[2]
            entity.container.position = {
                'x': to_follow.currentState.x + offsetx,
                'y': to_follow.currentState.y + offsety
            }
        }


    }

    handleFrameData(frameInfo, data) {
        if (data === undefined) {
            const registered = new Map(this.previousFrame.registered)
            const frame = {registered, number: frameInfo.number}
            this.previousFrame = frame
            return frame
        }
        const newRegistration = data[0] || new Map()
        const registered = new Map(this.previousFrame.registered)
        Object.keys(newRegistration).forEach(
            (k) => {
                registered.set(parseInt(k), newRegistration[k])
            }
        )
        const frame = {registered, number: frameInfo.number}
        this.previousFrame = frame
        return frame
    }

    reinitScene() {
        if (this.currentUpdateProgress !== undefined) {
            this.updateScene(this.previousUpdateData, this.currentUpdateFrame, 1)
        }
    }

}