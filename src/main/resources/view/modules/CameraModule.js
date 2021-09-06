import {WIDTH, HEIGHT} from '../core/constants.js'
import {api as entityModule} from '../entity-module/GraphicEntityModule.js'


export class CameraModule {
    constructor(assets) {
        this.container = {id : -1, sizeX : -1, sizeY : -1}
        this.cameraOffset = 0
        this.previousFrame = {
            registered: new Map()
        }
        this.lastProgress = 1
        this.lastFrame = 0
    }

    static get name() {
        return 'c'
    }


    updateScene(previousData, currentData, progress) {

        this.currentFrame = currentData
        this.currentProgress = progress
        console.log(entityModule.entities)

        if (currentData.registered.size !== 0 && (currentData.container.entity !== null)) {

            let maxX, minX, minY, maxY;
            let first = true;
            entityModule.entities.forEach(
                entity => {

                    if (currentData.registered.get(entity.id + "")) {
                        console.log(`added entity ${entity.id} which is at x = ${entity.currentState.x}, y = ${entity.currentState.y}`)
                        if (first) {
                            minX = maxX = entity.currentState.x
                            minY = maxY = entity.currentState.y
                            first = false
                        } else {
                            minX = Math.min(minX, entity.currentState.x)
                            minY = Math.min(minY, entity.currentState.y)
                            maxX = Math.max(maxX, entity.currentState.x)
                            maxY = Math.max(maxY, entity.currentState.y)
                        }

                    }
                }
            )
            const averagePoint = {x: (maxX + minX) / 2, y: (maxY + minY) / 2}
            console.log(averagePoint)
            const boundSize = {x: maxX - minX, y: maxY - minY}
            //Curve curve = oldCameraPosition.getDist(averagePoint) > CAMERA_OFFSET ? EASE_OUT : LINEAR;
            const scale = Math.min(HEIGHT / (boundSize.y + currentData.cameraOffset), WIDTH / (boundSize.x + currentData.cameraOffset))
            currentData.container.entity.graphics.scale.x = scale
            currentData.container.entity.graphics.scale.y = scale
            // if position is not relative del container.entity.x
            const newX = (currentData.container.sizeX/2 - averagePoint.x)*scale - (scale-1) * currentData.container.sizeX/2
            const newY = (currentData.container.sizeY/2 - averagePoint.y)*scale - (scale-1) * currentData.container.sizeY/2
            // currentData.container.entity.graphics.scale.x = currentData.container.entity.graphics.scale.y = 0.5
            currentData.container.entity.graphics.x = Math.round(newX)
            currentData.container.entity.graphics.y = Math.round(newY)
            console.log(`set the container to x : ${newX}, y : ${newY}, scale : ${scale}`)
        }
    }

    handleFrameData(frameInfo, data) {
        if (data === undefined) {
            const registered = new Map(this.previousFrame.registered)

            const cameraOffset = this.cameraOffset
            const container = this.container.id !== -1 ? {entity : entityModule.entities.get(this.container.id),
                sizeX : this.container.sizeX, sizeY : this.container.sizeY} : null
            const frame = {registered, number: frameInfo.number, cameraOffset, container}
            this.previousFrame = frame
            return frame
        }
        // const newRegistration = data[0] === undefined ? new Map() : data[0]
        const newRegistration = data[0] || new Map()
        const registered = new Map(this.previousFrame.registered)
        Object.keys(newRegistration).forEach(
            (k) => {
                registered.set(k, newRegistration[k])
            }
        )
        this.cameraOffset = data[1] || this.cameraOffset
        this.container = data[2]? {id : data[2][0], sizeX : data[2][1], sizeY : data[2][2]} : this.container
        const cameraOffset = this.cameraOffset
        const container = this.container.id !== -1 ? {entity : entityModule.entities.get(this.container.id),
            sizeX : this.container.sizeX, sizeY : this.container.sizeY} : null
        const frame = {registered, number: frameInfo.number, cameraOffset, container}
        this.previousFrame = frame
        return frame
    }

    reinitScene() {

    }

}